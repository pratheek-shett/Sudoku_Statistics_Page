# Sudoku Statistics App

This is an Android app that fetches and displays user statistics for a Sudoku game. The app is connected to Firebase Realtime Database, allowing it to dynamically fetch data such as total wins, average completion times, and total games played. It also handles scenarios where no data is available and displays a "No Data" screen when applicable.

## Features

- **Firebase Integration**: Fetches real-time data from Firebase to display user statistics.
- **Statistics Dashboard**: Shows user statistics such as total wins, best completion time, total games played, average completion time, average mistakes, and total hours played.
- **"No Data" Handling**: If no data is available, the app displays a fallback UI with a "No Data" message.
- **Real-time Data Updates**: The statistics page updates in real-time whenever the data in Firebase changes.
- **Internet Connection Status**: It has a dedicated page if no internet is available.


**Loading Progress Bar with background blur**

![Screenshot_20241018-150347](https://github.com/user-attachments/assets/e2c51008-00b9-4417-a019-40cff48d6038)

**No Internet Connected**

![Screenshot_20241018-150357](https://github.com/user-attachments/assets/29001e29-e79b-42c7-a19d-a19950f1714b)

**Statistics Page with Toast**

![Screenshot_20241018-150412](https://github.com/user-attachments/assets/1138f626-9294-4f97-b5d8-6bacf6e20d18)

**404 Error Page**

![Screenshot_20241018-150236](https://github.com/user-attachments/assets/9b9d45fd-8d27-48fe-9324-09ec5c9852d1)

## Installation

To install and run this app locally:

Clone the repository:
   ```bash
   git clone https://github.com/pratheek-shett/Sudoku_Statistics_Page.git
