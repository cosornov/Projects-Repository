#ifndef HISTORYVIEWCONTROLLER_H
#define HISTORYVIEWCONTROLLER_H

#include "Controllers/HistoryViewController.h"
#include "QSqlQueryModel"
#include <QString>

#include "Backend/DatabaseController.h"
#include "QSqlQueryModel"
#include <QString>
#include <vector>

/**
 * \class HistoryViewController
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief class that acts as a controller for the History View
 */
class HistoryViewController
{

public:

    /**
     * \brief HistoryViewController constructor that generates a new HistoryViewController
     */
    HistoryViewController();
    /**
     * \brief getTicketHistory gets all previous versions of a specific ticket
     * \param ticketID ID of ticket to get history
     * \return Model of tickets history
     */
    QSqlQueryModel* getTicketHistory(QString ticketID);

    /**
     * \brief getValueNames gets specific category and severity based on their index
     * \param category number associated with specific category
     * \param severity number associated with specifc severity
     * \return category and severity
     */
    std::vector<QString> getValueNames(QString category, QString severity);

private:

    /**
     * \brief _db Reference to the Frogers database.
     */
    DatabaseController *_db;

};

#endif // HISTORYVIEWCONTROLLER_H
