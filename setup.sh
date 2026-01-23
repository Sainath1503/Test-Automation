#!/bin/bash

# Setup Script for Test Automation Framework
# This script installs prerequisites and sets up the project

set -e

echo "=========================================="
echo "Test Automation Framework - Setup Script"
echo "=========================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Detect OS
OS="$(uname -s)"
case "${OS}" in
    Linux*)     MACHINE=Linux;;
    Darwin*)    MACHINE=Mac;;
    *)          MACHINE="UNKNOWN:${OS}"
esac

echo -e "${BLUE}Detected OS: $MACHINE${NC}"
echo ""

# Function to check if command exists
command_exists() {
    command -v "$1" &> /dev/null
}

# Function to install Java on Linux
install_java_linux() {
    echo -e "${YELLOW}Installing Java 17...${NC}"
    if command_exists apt-get; then
        sudo apt-get update
        sudo apt-get install -y openjdk-17-jdk
    elif command_exists yum; then
        sudo yum install -y java-17-openjdk-devel
    elif command_exists dnf; then
        sudo dnf install -y java-17-openjdk-devel
    else
        echo -e "${RED}Package manager not found. Please install Java 17 manually.${NC}"
        return 1
    fi
    
    # Set JAVA_HOME
    JAVA_HOME=$(readlink -f /usr/bin/java | sed "s:bin/java::")
    export JAVA_HOME
    echo "export JAVA_HOME=$JAVA_HOME" >> ~/.bashrc
    echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> ~/.bashrc
    echo -e "${GREEN}Java 17 installed successfully${NC}"
}

# Function to install Maven on Linux
install_maven_linux() {
    echo -e "${YELLOW}Installing Maven...${NC}"
    if command_exists apt-get; then
        sudo apt-get install -y maven
    elif command_exists yum; then
        sudo yum install -y maven
    elif command_exists dnf; then
        sudo dnf install -y maven
    else
        echo -e "${RED}Package manager not found. Please install Maven manually.${NC}"
        return 1
    fi
    echo -e "${GREEN}Maven installed successfully${NC}"
}

# Function to install Docker on Linux
install_docker_linux() {
    echo -e "${YELLOW}Installing Docker...${NC}"
    if command_exists docker; then
        echo -e "${GREEN}Docker is already installed${NC}"
        return 0
    fi
    
    # Install Docker using official script
    curl -fsSL https://get.docker.com -o get-docker.sh
    sudo sh get-docker.sh
    rm get-docker.sh
    
    # Add current user to docker group
    sudo usermod -aG docker $USER
    echo -e "${GREEN}Docker installed successfully${NC}"
    echo -e "${YELLOW}Note: You may need to log out and log back in for Docker group changes to take effect${NC}"
}

# Function to install Java on macOS
install_java_mac() {
    echo -e "${YELLOW}Installing Java 17...${NC}"
    if command_exists brew; then
        brew install openjdk@17
        sudo ln -sfn /opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
        echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
        echo 'export JAVA_HOME="/opt/homebrew/opt/openjdk@17"' >> ~/.zshrc
        echo -e "${GREEN}Java 17 installed successfully${NC}"
    else
        echo -e "${RED}Homebrew not found. Please install Homebrew first or install Java manually.${NC}"
        return 1
    fi
}

# Function to install Maven on macOS
install_maven_mac() {
    echo -e "${YELLOW}Installing Maven...${NC}"
    if command_exists brew; then
        brew install maven
        echo -e "${GREEN}Maven installed successfully${NC}"
    else
        echo -e "${RED}Homebrew not found. Please install Homebrew first or install Maven manually.${NC}"
        return 1
    fi
}

# Function to install Docker on macOS
install_docker_mac() {
    echo -e "${YELLOW}Installing Docker...${NC}"
    if command_exists brew; then
        brew install --cask docker
        echo -e "${GREEN}Docker Desktop installed successfully${NC}"
        echo -e "${YELLOW}Please start Docker Desktop from Applications${NC}"
    else
        echo -e "${RED}Homebrew not found. Please install Docker Desktop manually from https://www.docker.com/products/docker-desktop${NC}"
        return 1
    fi
}

# Main installation logic
echo "Checking prerequisites..."
echo ""

# Check and install Java
if ! command_exists java; then
    echo -e "${YELLOW}Java is not installed.${NC}"
    read -p "Do you want to install Java 17? (y/n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        if [ "$MACHINE" = "Linux" ]; then
            install_java_linux
        elif [ "$MACHINE" = "Mac" ]; then
            install_java_mac
        else
            echo -e "${RED}Automatic Java installation is not supported on this OS. Please install Java 17 manually.${NC}"
        fi
    fi
else
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -lt 17 ]; then
        echo -e "${YELLOW}Java version is less than 17. Please upgrade to Java 17+.${NC}"
    else
        echo -e "${GREEN}Java is installed${NC}"
    fi
fi
echo ""

# Check and install Maven
if ! command_exists mvn; then
    echo -e "${YELLOW}Maven is not installed.${NC}"
    read -p "Do you want to install Maven? (y/n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        if [ "$MACHINE" = "Linux" ]; then
            install_maven_linux
        elif [ "$MACHINE" = "Mac" ]; then
            install_maven_mac
        else
            echo -e "${RED}Automatic Maven installation is not supported on this OS. Please install Maven manually.${NC}"
        fi
    fi
else
    echo -e "${GREEN}Maven is installed${NC}"
fi
echo ""

# Check and install Docker
if ! command_exists docker; then
    echo -e "${YELLOW}Docker is not installed.${NC}"
    read -p "Do you want to install Docker? (y/n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        if [ "$MACHINE" = "Linux" ]; then
            install_docker_linux
        elif [ "$MACHINE" = "Mac" ]; then
            install_docker_mac
        else
            echo -e "${RED}Automatic Docker installation is not supported on this OS. Please install Docker manually.${NC}"
        fi
    fi
else
    echo -e "${GREEN}Docker is installed${NC}"
    # Check if Docker daemon is running
    if ! docker info &> /dev/null; then
        echo -e "${YELLOW}Docker daemon is not running. Please start Docker Desktop or Docker daemon.${NC}"
    fi
fi
echo ""

# Verify Docker Compose
if ! docker compose version &> /dev/null && ! command_exists docker-compose; then
    echo -e "${YELLOW}Docker Compose is not available.${NC}"
    echo "Docker Compose should be included with Docker. Please ensure Docker is properly installed."
fi
echo ""

# Create necessary directories
echo "Creating necessary directories..."
mkdir -p target/screenshots
mkdir -p target/reports
mkdir -p target/logs
mkdir -p target/api-comparison
echo -e "${GREEN}Directories created${NC}"
echo ""

# Final verification
echo "=========================================="
echo "Running final prerequisite check..."
echo "=========================================="
./check-prerequisites.sh

if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}=========================================="
    echo "Setup completed successfully!"
    echo "==========================================${NC}"
    echo ""
    echo "Next steps:"
    echo "1. Run './pull-containers.sh' to pull Docker images"
    echo "2. Run './run-project.sh' to start the project"
    echo ""
else
    echo ""
    echo -e "${YELLOW}Some prerequisites are still missing.${NC}"
    echo "Please install them manually and run './check-prerequisites.sh' again."
    echo ""
fi
