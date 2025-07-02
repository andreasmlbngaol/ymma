package org.andreasmlbngaol.ymma.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.ApplicationConfig
import org.andreasmlbngaol.ymma.database.tables.Colleges
import org.andreasmlbngaol.ymma.database.tables.Comments
import org.andreasmlbngaol.ymma.database.tables.CourseMemberships
import org.andreasmlbngaol.ymma.database.tables.CourseScheduleOverrides
import org.andreasmlbngaol.ymma.database.tables.CourseSchedules
import org.andreasmlbngaol.ymma.database.tables.Courses
import org.andreasmlbngaol.ymma.database.tables.Departments
import org.andreasmlbngaol.ymma.database.tables.Faculties
import org.andreasmlbngaol.ymma.database.tables.Notifications
import org.andreasmlbngaol.ymma.database.tables.PostAttachments
import org.andreasmlbngaol.ymma.database.tables.Posts
import org.andreasmlbngaol.ymma.database.tables.Users
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object DatabaseFactory {
    fun init(config: ApplicationConfig) {
        val dbConfig = config.config("ktor.database")
        println("URL: " + dbConfig.property("url").getString())
        println("User: " + dbConfig.property("user").getString())


        val hikariConfig = HikariConfig().apply {
            jdbcUrl = dbConfig.property("url").getString()
            driverClassName = dbConfig.property("driver").getString()
            username = dbConfig.property("user").getString()
            password = dbConfig.property("password").getString()
            maximumPoolSize = dbConfig.property("maxPoolSize").getString().toInt()
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }
        val datasource = HikariDataSource(hikariConfig)
        Database.connect(datasource)
        createTableIfNotExist()

    }

    fun createTableIfNotExist() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                Colleges,
                Comments,
                CourseMemberships,
                Courses,
                CourseScheduleOverrides,
                CourseSchedules,
                Departments,
                Faculties,
                Notifications,
                PostAttachments,
                Posts,
                Users
            )
        }

    }
}