# Network Packet Sniffer

Network Packet Sniffer is a tool designed to capture and analyze network packets, providing detailed statistics and insights into network traffic. The tool includes features such as analyzing application layer protocol ratios and monitoring free memory.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Overview

Network Packet Sniffer is built in Java and leverages the Jpcap library to capture network packets. It can analyze the captured packets to provide insights into the protocols used and memory usage.

## Features

- Capture network packets in real-time.
- Analyze application layer protocols.
- Display statistics such as:
- Number of packets per protocol.
- Percentage of packets per protocol.
- Total packet size per protocol.
- Percentage of packet size per protocol.
- Monitor and display free memory statistics.

## Requirements

- Java JDK (version 8 or higher)
- Jpcap library

## Installation

1. **Clone the repository:**
   ```sh
   git clone https://github.com/swetha260/Network-Packet-Sniffer.git
   ```

2. **Navigate to the project directory:**
   ```sh
   cd Network-Packet-Sniffer
   ```

3. **Add Jpcap library to your project:**
   - Download Jpcap from [Jpcap SourceForge](http://netresearch.ics.uci.edu/kfujii/jpcap/doc/install.html).
   - Follow the installation instructions specific to your operating system.

4. **Set the `java.library.path` to include Jpcap:**
   - Ensure Jpcap's native libraries are in your system's library path.

## Usage

1. **Open the project in Eclipse:**
   - Open Eclipse IDE.
   - Import the project by selecting `File` > `Import` > `Existing Projects into Workspace`.

2. **Run the `NetPackSniff` class:**
   - Navigate to `src/netpacksniff/NetPackSniff.java`.
   - Right-click on the `NetPackSniff` class and select `Run As` > `Java Application`.

3. **Capturing and Analyzing Packets:**
   - The application will open a GUI window.
   - Start capturing packets by selecting the appropriate network interface.

## Contributing

We welcome contributions to improve Network Packet Sniffer. To contribute, follow these steps:

1. **Fork the repository:**
   - Click the "Fork" button at the top right corner of the repository page.

2. **Create a new branch:**
   ```sh
   git checkout -b feature-branch
   ```

3. **Make your changes:**
   - Implement your feature or fix the bug.

4. **Commit your changes:**
   ```sh
   git commit -m 'Add new feature'
   ```

5. **Push to the branch:**
   ```sh
   git push origin feature-branch
   ```

6. **Create a Pull Request:**
   - Go to the repository page on GitHub.
   - Click the "Pull Request" button and follow the prompts.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE.txt) file for details.

```

### Notes:
1. Replace `swetha260` with your actual GitHub username.
2. Make sure to include a `LICENSE` file in your repository if you mention it in the README. You can generate one using GitHub’s license templates.
3. If there are specific installation steps or dependencies required for Jpcap or other parts of your project, consider including detailed instructions or links to resources in the README.
