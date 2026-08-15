@echo off
echo ========================================================
echo Starting DocMind Full-Stack Application...
echo ========================================================

echo Starting Python AI Microservice (Port 8000)...
start "DocMind AI Service" cmd /k "cd /d %~dp0ai-service && python main.py"

echo Starting Java Spring Boot Backend (Port 8080)...
start "DocMind Backend" cmd /k "cd /d %~dp0backend && mvnw.cmd spring-boot:run"

echo Starting React Frontend (Port 5173)...
start "DocMind Frontend" cmd /k "cd /d %~dp0frontend && npm install && npm run dev"

echo.
echo All services launching in separate windows!
echo - AI Service:  http://localhost:8000
echo - Backend:     http://localhost:8080
echo - Frontend:    http://localhost:5173
echo ========================================================
