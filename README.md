# HorseRaceSimulator

This is a graphical and textual Horse Race Simulator written in Java.
The program allows users to configure racing tracks, customize horses, and visualize the race in real time using a GUI.
Players can also experience dynamic weather conditions, different track shapes, and horse performance influenced by custom attributes.
## Features

#### Real-Time Race Visualisation: 
Java Swing-based GUI dynamically shows horse movement across different track types (Oval, Figure-eight, Custom).
#### Horse Customisation:
Users can set each horse's:
- Name
- Confidence level
- Breed (Thoroughbred, Arabian, Quarter Horse)
- Coat color
- Equipment (Saddle, Horseshoe)
- Symbol for display in the race
#### Dynamic Track Settings:
Players can customize:
- Number of lanes (up to 1000)
- Track length (meters)
- Track shape (Oval, Figure-eight, Custom)
- Track condition (Dry, Muddy, Icy)
#### Performance Effects:
Horse attributes and equipment affect speed, risk of falling, and confidence.
#### Simple, User-Friendly GUI:
Intuitive layout using buttons, text fields, and dropdowns for configuration.
## Classes Overview

### Part 1 (Text-Based Simulation)
#### Horse.java:
Defines horse attributes and logic for moving, falling, and winning.
Confidence adjustments occur based on race performance.
#### Race.java:
Manages the race state, controls horse movement, prints race progress in console.
#### Main.java:
Starts a sample text-based race using Race and Horse classes.
### Part 2 (Graphical Simulation)
#### RaceGUI.java:
Provides a full GUI for track and horse customization, betting, and race visualization.
#### RaceTrackPanel.java:
Custom JPanel for drawing the race tracks and horses graphically based on race progress.
## Installation and Setup

1. Install Java JDK 17+ on your device.
2. Install an IDE like IntelliJ IDEA or Visual Studio Code.
3. Clone or download this repository.
4. Open the project in your IDE.
5. Compile all .java files inside Part1 and Part2.
6. To run:
- For text-based simulation, run Main.java.
- For GUI-based simulation, run RaceGUI.java.
## How to Play (GUI Version)

1. Run the RaceGUI main method.
2. Configure your track:
- Choose number of lanes, track length, track shape, and track condition.
- Click "Save Track Settings".
3. Customize and add horses:
- Set horse name, confidence, breed, coat color, symbol, and equipment.
- Assign each horse to a lane.
4. Start the race:
- Click "Start Race" to watch the simulation!
5. After the race:
- View results including each horse's performance, speed, and win/loss status.
## Notes

- Each horse's movement and fall chances are affected by track shape, track condition, breed, and equipment.
- If all horses fall during the race, the race ends automatically.