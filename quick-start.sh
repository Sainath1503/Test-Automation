#!/bin/bash

# Quick Start Script
# This script performs a complete setup and run in one go

set -e

echo "=========================================="
echo "Test Automation Framework - Quick Start"
echo "=========================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Step 1: Check prerequisites
echo -e "${BLUE}Step 1: Checking prerequisites...${NC}"
if [ -f "./check-prerequisites.sh" ]; then
    ./check-prerequisites.sh
    if [ $? -ne 0 ]; then
        echo -e "${RED}Prerequisites check failed. Please install missing requirements.${NC}"
        echo "Run './setup.sh' for automated installation (Linux/macOS only)."
        exit 1
    fi
else
    echo -e "${YELLOW}check-prerequisites.sh not found, skipping...${NC}"
fi
echo ""

# Step 2: Pull containers
echo -e "${BLUE}Step 2: Pulling Docker containers...${NC}"
if [ -f "./pull-containers.sh" ]; then
    ./pull-containers.sh
    if [ $? -ne 0 ]; then
        echo -e "${RED}Failed to pull containers${NC}"
        exit 1
    fi
else
    echo -e "${YELLOW}pull-containers.sh not found, skipping...${NC}"
fi
echo ""

# Step 3: Run project
echo -e "${BLUE}Step 3: Starting and running the project...${NC}"
if [ -f "./run-project.sh" ]; then
    ./run-project.sh "$@"
else
    echo -e "${RED}run-project.sh not found${NC}"
    exit 1
fi

echo ""
echo -e "${GREEN}Quick start completed!${NC}"
