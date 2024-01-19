![COURSES_](/assets/png/logo-no-background.png)

by Felix Ramnelöv

## Welcome

Welcome to my fullstack-project 'COURSES\_', a multi-user application for students to handle your university courses and monitoring data such as average grade and total amount of credits.

## The idea

The idea is to have this application as a side project in combination with my studies as a M.Sc. student in CSE. The application both involves backend with a **Java Spring Boot** rest application to access as **PostgreSQL** database and frontend using **React** with **Typescript** and **Bootstrap**.

The application will support multiple users at the same time with high security using password encryption and JSON Web Tokens.

Users will be able to have different roles, one being 'admin' whom can manage users, but without accessing sensetive data such as grades to respect user integrity.

Also a vast amount of focus will be put in the user experience and making sure that the application is fail proof for users of all levels.

## The plan

The plan is to start with implementing the user-part of the application, beginning with the backend. This is because the user-level is the highest of all, each user has courses, and each course has examinations. Beginning with e.g. courses and building a first prototype would lead to extra work.

## Progress

Thus far the backend for users have been implemented and tested, and
with it the API for both users and admins. As a user you have the
ability to register, login, update your information and delete your
account and as an admin you can do the same with the addition of
managing and viewing all other users.

In the frontend a first prototype of the user-part has been implemented
with the ability to register and login. It has been prepared to
implement the management of your own account and the extra functionality
of an admin.

Down bellow you can view a demo of the current state of the application,
a prototype of implemented functionality. A good amount of focus has
been put into the user experience and e.g. making sure the creation of
an account is done in a safe and secure way.

[![COURSES_ demo-0.1](https://i9.ytimg.com/vi_webp/-iZi9EfzOwQ/mqdefault.webp?v=65aaf25a&sqp=CIzkq60G&rs=AOn4CLANkfQyWUeHfS0p8gqXnN5TiXwlxA)](https://youtu.be/-iZi9EfzOwQ)

## Contact

You can either contact me via [e-mail](mailto:felix@ramnelov.com) or [LinkedIn](https://www.linkedin.com/in/felixramnelöv/).
