# PowerShell script to compile and launch the Student Skill & Career Tracker JavaFX App
$ErrorActionPreference = "Stop"

$root = Join-Path $PSScriptRoot ".."
$srcDir = Join-Path $root "src\main\java"
$libDir = Join-Path $root "lib"
$resDir = Join-Path $root "src\main\resources"
$binDir = Join-Path $root "target\classes"

if (-not (Test-Path $libDir)) {
    Write-Host "Downloading library dependencies first..."
    & (Join-Path $PSScriptRoot "setup_libs.ps1")
}

if (-not (Test-Path $binDir)) {
    New-Item -ItemType Directory -Path $binDir | Out-Null
}

# Copy CSS resource to classes
$stylesCss = Join-Path $resDir "styles.css"
if (Test-Path $stylesCss) {
    Copy-Item $stylesCss $binDir -Force
}

$libJars = Get-ChildItem -Path $libDir -Filter "*.jar" | Select-Object -ExpandProperty FullName
$classpath = ($libJars -join ";")

Write-Host "Compiling JavaFX Application..."
$javaFiles = Get-ChildItem -Path $srcDir -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName
javac -d $binDir -cp $classpath $javaFiles

if ($LASTEXITCODE -ne 0) {
    Write-Error "Compilation Failed."
    exit 1
}

Write-Host "Launching JavaFX GUI Application..."
$appClasspath = "$binDir;$classpath"
$modulePath = $libDir

java --module-path $modulePath --add-modules javafx.controls,javafx.fxml -cp $appClasspath com.tracker.ui.MainApp
