# Use a known-good OpenJDK base image
FROM eclipse-temurin:21-jdk

# Optional: set up display (for GUI forwarding, Windows/macOS + X server)
ENV DISPLAY=host.docker.internal:0.0

# Install required dependencies
RUN apt-get update && \
    apt-get install -y maven wget unzip libgtk-3-0 libgbm1 libx11-6 && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Download and install JavaFX SDK 21
RUN wget https://download2.gluonhq.com/openjfx/21/openjfx-21_linux-x64_bin-sdk.zip -O /tmp/openjfx.zip && \
    unzip /tmp/openjfx.zip -d /opt && \
    rm /tmp/openjfx.zip

# Confirm extraction
RUN ls -l /opt/javafx-sdk-21/lib

# Set working directory
WORKDIR /app

# Copy project
COPY pom.xml .
COPY src ./src

# Build with Maven (skip tests)
RUN mvn clean package -DskipTests

# Find the actual JAR name dynamically
RUN ls -l target

# Run the JavaFX app (adjust JAR name if needed)
CMD ["java", "--module-path", "/opt/javafx-sdk-21/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "target/bmidemo.jar"]
