#ifndef MAINCONTROLLER_H
#define MAINCONTROLLER_H


#include "Backend/DatabaseController.h"
#include <QSqlQuery>
#include <QSqlQueryModel>

/**
 * \class DashboardController
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief class that acts as a controller for the Dashboard
 */

class DashboardController
{
public:

    /**
     * \brief This constructor initializes the DashboardController
     */
    DashboardController();

    /**
     * \brief loadTickets gets tickets from database
     * \param currentIndex how many tickets to get
     * \param offset where to start
     * \return model to place in table
     */
    QSqlQueryModel* loadTickets(QString currentIndex, QString offset);

private:

    /**
     * \brief _db Reference to the Frogers database.
     *
     */
    DatabaseController *_db;
};

#endif // MAINCONTROLLER_H
