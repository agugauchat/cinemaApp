# CinemaApp

Android app with MVVM architecture.

<img src="https://github.com/agugauchat/cinemaApp/assets/18156523/818360e9-281a-43d6-afdf-71f5c0004ad7" alt="drawing" width="400"/>


Apk [here](https://drive.google.com/file/d/1VZlNK_MQ1GC8KLsclAP_b-V2IPL7LKtb/view?usp=sharing).

## Notes

To simplify implementation, CinemaApp assumes the following:

- **Single-Day Projection:** All cinema rooms project all movies on the same day, with different showtimes.

- **Uniform Room Capacity:** All cinema rooms have the same seating capacity.

- **Uniform Base Ticket Price:** The cinema has a consistent base ticket price for all movies, which varies according to the day of the week.

As an iteration of the app,  the list of movies could be fetched from a web service, along with cinema information (rooms, capacity, price, promotions, etc).
This can be achieved without the need to modify the UI layer; you just need to update the implementation in the corresponding repository.


#### **Author:** Augusto Gauchat (agu.gauchat@gmail.com)
