# PowerShell script to download standalone JavaFX and JUnit dependencies into lib/
$ErrorActionPreference = "Stop"
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12

$libDir = Join-Path $PSScriptRoot "..\lib"
if (-not (Test-Path $libDir)) {
    New-Item -ItemType Directory -Path $libDir | Out-Null
}

$urls = @(
    "https://repo1.maven.org/maven2/org/openjfx/javafx-controls/21.0.1/javafx-controls-21.0.1.jar",
    "https://repo1.maven.org/maven2/org/openjfx/javafx-controls/21.0.1/javafx-controls-21.0.1-win.jar",
    "https://repo1.maven.org/maven2/org/openjfx/javafx-graphics/21.0.1/javafx-graphics-21.0.1.jar",
    "https://repo1.maven.org/maven2/org/openjfx/javafx-graphics/21.0.1/javafx-graphics-21.0.1-win.jar",
    "https://repo1.maven.org/maven2/org/openjfx/javafx-base/21.0.1/javafx-base-21.0.1.jar",
    "https://repo1.maven.org/maven2/org/openjfx/javafx-base/21.0.1/javafx-base-21.0.1-win.jar",
    "https://repo1.maven.org/maven2/org/openjfx/javafx-fxml/21.0.1/javafx-fxml-21.0.1.jar",
    "https://repo1.maven.org/maven2/org/openjfx/javafx-fxml/21.0.1/javafx-fxml-21.0.1-win.jar",
    "https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-api/5.10.1/junit-jupiter-api-5.10.1.jar",
    "https://repo1.maven.org/maven2/org/opentest4j/opentest4j/1.3.0/opentest4j-1.3.0.jar",
    "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-commons/1.10.1/junit-platform-commons-1.10.1.jar",
    "https://repo1.maven.org/maven2/org/junit/jupiter/junit-jupiter-engine/5.10.1/junit-jupiter-engine-5.10.1.jar",
    "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-engine/1.10.1/junit-platform-engine-1.10.1.jar",
    "https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.10.1/junit-platform-console-standalone-1.10.1.jar",
    "https://repo1.maven.org/maven2/org/apiguardian/apiguardian-api/1.1.2/apiguardian-api-1.1.2.jar"
)

foreach ($url in $urls) {
    $fileName = Split-Path $url -Leaf
    $dest = Join-Path $libDir $fileName
    if (-not (Test-Path $dest)) {
        Write-Host "Downloading $fileName ..."
        Invoke-WebRequest -Uri $url -OutFile $dest
    } else {
        Write-Host "$fileName already exists."
    }
}

Write-Host "All library JARs successfully configured in $libDir"
