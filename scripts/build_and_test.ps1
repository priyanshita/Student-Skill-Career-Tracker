# PowerShell script to compile sources and run JUnit tests
$ErrorActionPreference = "Stop"

$root = Join-Path $PSScriptRoot ".."
$srcDir = Join-Path $root "src\main\java"
$testDir = Join-Path $root "src\test\java"
$libDir = Join-Path $root "lib"
$binDir = Join-Path $root "target\classes"
$testBinDir = Join-Path $root "target\test-classes"

if (Test-Path $binDir) { Remove-Item -Recurse -Force $binDir }
if (Test-Path $testBinDir) { Remove-Item -Recurse -Force $testBinDir }

New-Item -ItemType Directory -Path $binDir | Out-Null
New-Item -ItemType Directory -Path $testBinDir | Out-Null

$libJars = Get-ChildItem -Path $libDir -Filter "*.jar" | Select-Object -ExpandProperty FullName
$classpath = ($libJars -join ";")

Write-Host "Compiling main Java sources..."
$javaFiles = Get-ChildItem -Path $srcDir -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName
javac -d $binDir -cp $classpath $javaFiles

if ($LASTEXITCODE -ne 0) {
    Write-Error "Main Compilation Failed."
    exit 1
}

Write-Host "Compiling test sources..."
$testFiles = Get-ChildItem -Path $testDir -Recurse -Filter "*.java" | Select-Object -ExpandProperty FullName
$testClasspath = "$binDir;$classpath"
javac -d $testBinDir -cp $testClasspath $testFiles

if ($LASTEXITCODE -ne 0) {
    Write-Error "Test Compilation Failed."
    exit 1
}

Write-Host "Running JUnit 5 Test Suite..."
$runnerCp = "$binDir;$testBinDir;$classpath"
$junitJar = Get-ChildItem -Path $libDir -Filter "junit-platform-console-standalone-*.jar" | Select-Object -First 1 -ExpandProperty FullName

java -jar $junitJar --class-path $runnerCp --scan-class-path

Write-Host "Build and test completed successfully!"
