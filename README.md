# Sudoku Statistics App

This is an Android app that fetches and displays user statistics for a Sudoku game. The app is connected to Firebase Realtime Database, allowing it to dynamically fetch data such as total wins, average completion times, and total games played. It also handles scenarios where no data is available and displays a "No Data" screen when applicable.

## Features

- **Firebase Integration**: Fetches real-time data from Firebase to display user statistics.
- **Statistics Dashboard**: Shows user statistics such as total wins, best completion time, total games played, average completion time, average mistakes, and total hours played.
- **"No Data" Handling**: If no data is available, the app displays a fallback UI with a "No Data" message.
- **Real-time Data Updates**: The statistics page updates in real-time whenever the data in Firebase changes.
- **Internet Connection Status**: It has dedicated page if no internet is available.

## Screenshots

![Screenshot_20241018-150347](https://github.com/user-attachments/assets/e2c51008-00b9-4417-a019-40cff48d6038)


## Installation

To install and run this app locally:

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/sudoku-statistics-app.git
