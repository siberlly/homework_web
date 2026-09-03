param(
    [switch]$BuildOnly
)

$ErrorActionPreference = "Stop"
$projectPath = $PSScriptRoot
$mavenPath = Join-Path $projectPath ".maven\maven-3.9.16\bin\mvn.cmd"
$escapedProjectPath = [regex]::Escape($projectPath)

function Stop-ProjectJetty {
    $jettyProcesses = Get-CimInstance Win32_Process -Filter "Name = 'java.exe'" |
        Where-Object {
            $_.CommandLine -match $escapedProjectPath -and
            $_.CommandLine -match 'jetty:run|jetty-ee10'
        }

    foreach ($process in $jettyProcesses) {
        Write-Host "Đang dừng Jetty cũ (PID $($process.ProcessId))..." -ForegroundColor Yellow
        Stop-Process -Id $process.ProcessId -Force -ErrorAction SilentlyContinue
    }
}

if (-not (Test-Path $mavenPath)) {
    $mavenCommand = Get-Command mvn.cmd -ErrorAction SilentlyContinue
    if ($null -eq $mavenCommand) {
        throw "Không tìm thấy Maven. Hãy cài Maven hoặc đặt Maven vào PATH."
    }
    $mavenPath = $mavenCommand.Source
}

Push-Location $projectPath
try {
    Stop-ProjectJetty

    Write-Host "Đang compile và đóng gói ứng dụng..." -ForegroundColor Cyan
    & $mavenPath clean package

    if ($LASTEXITCODE -ne 0) {
        throw "Compile thất bại với mã lỗi $LASTEXITCODE."
    }

    if ($BuildOnly) {
        Write-Host "Compile thành công." -ForegroundColor Green
        exit 0
    }

    $port = 8080
    if (Get-NetTCPConnection -LocalPort $port -State Listen -ErrorAction SilentlyContinue) {
        throw "Cổng 8080 vẫn đang được sử dụng bởi process khác."
    }

    Write-Host "Đang chạy ứng dụng tại http://localhost:$port/email-servlet/" -ForegroundColor Green
    & $mavenPath "-Djetty.http.port=$port" jetty:run
}
finally {
    Pop-Location
}
