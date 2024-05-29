# Currency Converter App

A simple currency converter app that allows users to convert between different currencies. 
The app fetches exchange rates and performs real-time conversions based on user input.

## Features

- Converts between multiple currencies.
- Fetches real-time exchange rates.
- Easy-to-use interface with a list of currencies.
- Displays converted amounts in a RecyclerView grid.
- Supports selecting the base currency from a spinner.

## Usage

1. **Enter the amount:**
    - Input the amount you want to convert in the EditText field at the top of the screen.

2. **Select the base currency:**
    - Choose the currency you want to convert from using the spinner below the EditText.

3. **View converted amounts:**
    - The app will display the converted amounts for various currencies in a RecyclerView grid below the spinner.

## Project Architecture

This project follows the MVVM (Model-View-ViewModel) architecture pattern combined with 
Clean Architecture principles to ensure a scalable and maintainable codebase.

### Architecture Overview

- **MVVM + Clean Architecture**:
    - **Model**: Defines the data structure and manages the business logic. This layer interacts with the data sources 
        (e.g., Room database, SharedPreferences, network) and provides data to the Domain Layer.
    - **Domain**: Contains the business logic of the application. It defines use cases which are the specific actions the application 
        can perform. These use cases are independent of the data sources and UI, making the application more modular and easier to test.
      - **View**: UI components that display the data. These are usually Activities, Fragments, and XML layouts in Android. The View observes 
        data changes from the ViewModel and updates the UI accordingly.
      - **ViewModel**: Acts as a bridge between the Model and the View, handling data logic and preparation. It holds UI-related data 
        and communicates with the Domain Layer to fetch and process data.

### Domain Layer

The Domain layer is a key component in Clean Architecture. It encapsulates the core business logic and rules of the application, 
providing a separation of concerns and promoting a more maintainable and testable codebase.

- **Entities**: The basic business model objects that are independent of the data layer and UI.
- **Use Cases (Interactors)**: These are the business logic handlers. Each use case represents a specific feature or 
     functionality of the application, such as converting an amount between currencies. The use cases orchestrate the flow of data to 
     and from the entities and ensure the business rules are applied.

### Technologies Used

- **Dependency Injection**: Hilt
- **Asynchronous Programming**: Coroutines + Flow
- **Local Storage**: SharedPreferences, Room
- **Jetpack Components**: LiveData, Navigation Component (Nav-Graph)

### Android Studio Version

- **Android Studio Jellyfish | 2023.3.1**
