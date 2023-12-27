# Entities and Attributes:
## Vehicles
- vehicle_id (INTEGER, Primary Key, Auto Increment): A unique identifier for each vehicle.
- brand (VARCHAR(30), Not Null): The brand of the vehicle.
- model (VARCHAR(30), Not Null): The model of the vehicle.
- color (VARCHAR(30), Not Null): The color of the vehicle.
- type (VARCHAR(10), Not Null): The type/category of the vehicle.
- lic_plate (VARCHAR(10), Not Null, Unique): The license plate number of the vehicle.
- range_km (INTEGER, Not Null): The driving range of the vehicle in kilometers.
- rented_count (INT): The total number of times the vehicle has been rented.
- total_days (INT): The total number of days the vehicle has been rented.
- quantity (INT, Not Null): The quantity of this type of vehicle available for rent.
- daily_rental_cost (INT, Not Null): The daily cost to rent the vehicle.
- daily_insurance_cost (INT, Not Null): The daily insurance cost for the vehicle.
- is_damaged (VARCHAR(7), Not Null): The damage status of the vehicle.
- subtype_name (VARCHAR(30), Foreign Key): The name of the subtype the vehicle belongs to, referencing subtype_name in the VehicleSubtypes table.
<br /><br />
## Users
- user_id (INTEGER, Primary Key, Auto Increment): A unique identifier for each user.
- username (VARCHAR(30), Not Null, Unique): The username of the user.
- password (VARCHAR(32), Not Null): The password for the user's account.
- name (VARCHAR(50), Not Null): The user's full name.
- birthdate (DATE, Not Null): The birth date of the user.
- address (VARCHAR(100), Not Null): The user's address.
- driv_lic (INT): The user's driving license number.
- credit_card (INT, Not Null): The user's credit card number.
<br /><br />
## Rentals
- rental_id (INTEGER, Primary Key, Auto Increment): A unique identifier for each rental.
- vehicle_id (INTEGER, Not Null, Foreign Key): References vehicle_id in the vehicles table.
- user_id (INTEGER, Not Null, Foreign Key): References user_id in the users table.
- driv_lic (INTEGER, Not Null): The driving license number used for the rental.
- rental_date (DATE, Not Null): The date when the vehicle was rented.
- duration (INTEGER, Not Null): The duration of the rental in days.
- total_cost (INTEGER, Not Null): The total cost of the rental.
- is_returned (VARCHAR(7), Not Null): Indicates whether the vehicle has been returned.
<br /><br />
## Maintenance
- maintenance_id (INTEGER, Primary Key, Auto Increment): A unique identifier for each maintenance record.
- vehicle_id (INTEGER, Not Null, Foreign Key): References vehicle_id in the vehicles table.
- cost (INTEGER, Not Null): The cost of the maintenance.
- start_date (DATE, Not Null): The start date of the maintenance.
- end_date (DATE, Not Null): The end date of the maintenance.
- maint_type (VARCHAR(15), Not Null): The type of maintenance performed.
- status (VARCHAR(7), Not Null): The status of the maintenance (e.g., completed, pending).
<br /><br />
## VehicleSubtypes
- subtype_name (VARCHAR(30), Primary Key, Unique, Not Null): The name of the vehicle subtype.
- capacity (INTEGER, Not Null): The capacity of the vehicle subtype (e.g., number of passengers).
<br /><br />
## Relationships:
- Vehicles to Rentals: One-to-Many (Each vehicle can have multiple rentals).
- Vehicles to Maintenance: One-to-Many (Each vehicle can have multiple maintenance records).
- Users to Rentals: One-to-Many (Each user can have multiple rentals).
- Vehicles to VehicleSubtypes: Many-to-One (Each vehicle can belong to one subtype).
