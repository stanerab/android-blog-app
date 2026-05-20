# Android Blog App

A native Android blogging application built in **Java** using **Android Studio**, developed as portfolio coursework for **6CS027 Secure Mobile Application Development** at the University of Wolverhampton.

The app supports **both offline and online** workflows. users can create, manage, and search blog posts entirely on-device, attach media from the camera or photo gallery, share content via Android's standard intent system, and upload posts to Facebook for online publishing.

**Final mark: 95% (Distinction)**

---

## Features

### Offline message management
- Create, edit, view, and delete blog posts
- View individual posts or the full list of posts
- Search across all post content with matching results
- Bulk select and delete multiple posts
- Persistent storage using **SQLite** on-device database

### Media integration
- Attach photos from the device **photo gallery**
- Capture and attach photos directly from the **camera**
- Display attached media inline within posts

### Content sharing
- Share individual posts via the standard Android **Share intent**, including email and other registered apps
- Share both text and image content together

### Online publishing
- Upload individual or grouped posts to a personal **Facebook** account
- Maintain offline-first behaviour with online sync when needed

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| IDE | Android Studio |
| Platform | Android (native) |
| Database | SQLite |
| Architecture | Activities, Fragments, Intents |
| External Integration | Facebook for Android (online publishing) |

---

## Module Context

This project was submitted for **6CS027 Secure Mobile Application Development** and assessed against the following learning outcomes:

- **LO1:** Offline message management and data persistence
- **LO2:** Integration of media with messaging functionality
- **LO3:** Android Intent system for content sharing
- **LO4:** Online data upload

**Marking breakdown:**

| Criterion | Score |
|---|---|
| App development (CRUD, search, list, delete) | 40/40 |
| SQLite storage | 10/10 |
| Photo / camera integration | 10/10 |
| Android Share intent | 10/10 |
| Online upload | 5/10 |
| Supporting documentation and demo video | 20/20 |
| **Total** | **95/100** |

---

## Demo

A short demonstration video of the working application is available via https://www.youtube.com/watch?v=kh93NAXIFpY&t=367s

---

## Author

**Stanley Erhabor**
Full Stack Developer
[Portfolio](https://portfolio.blueprintcaretech.com) | [GitHub](https://github.com/stanerab) | [LinkedIn](https://linkedin.com/in/stanleyerhabor)

---

## License

This repository is made public for portfolio and demonstration purposes only. Cloning, forking, copying, or redistributing any part of this code without explicit written permission is not permitted.

© 2026 Stanley Erhabor. All rights reserved.
