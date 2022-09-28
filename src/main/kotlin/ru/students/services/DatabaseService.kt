package ru.students.services

import org.ktorm.database.Database

class DatabaseService {
    companion object {
        private val database = Database.connect(
            url = "jdbc:postgresql://ec2-35-170-146-54.compute-1.amazonaws.com:5432/d28bqqjc4htcpb",
            driver = "org.postgresql.Driver",
            user = "liklyvzgmmdrzz",
            password = "384d3bb5735948ea450e941c43a98e8956b766df2d42f3c1b3deb1fe5259ef30"
        )

        fun getConnection(): Database = database;
    }

}