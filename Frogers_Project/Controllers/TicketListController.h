#ifndef TICKETLISTCONTROLLER_H
#define TICKETLISTCONTROLLER_H


#include "Backend/DatabaseController.h"
#include "QSqlQueryModel"
#include <QString>

/**
 * \class TicketListController
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief class that acts as a controller for the TicketList
 */
class TicketListController
{
public:

    /**
     * \brief Constructor that generates a new CustomerListController
     */
    TicketListController();

    /**
     * Loads the specified tickets into a model, depending on the beginning and the end offsets and
     * returns the model containing the specified amount of tickets
     * \param startIndex offset of the first row
     * \param endIndex offset of the last row
     * \return Returns the model containing the specified amount of tickets
     */
    QSqlQueryModel* loadTickets(QString startIndex, QString endIndex);

    /**
     * Loads the specified tickets into a model, depending on the applied filters and
     * returns the model containing the specified tickets
     * \param currentIndex
     * \param offset
     * \param addedFilters  Filters to be applied
     * \param addedFilterValues
     * \return Returns the model containing the specified tickets
     */
    QSqlQueryModel* loadTicketsbyFilters(QString currentIndex, QString offset, QString addedFilters [], QString addedFilterValues []);


    /**
     * @brief resultCount Returns the count of the ticket list
     * @return  Returns the count of the ticket list
     */
    int resultCount();

    /**
     * Updates  the category, severity, status, and  agent id fields of the specified ticket and
     * returns true the ticket was successfully updated
     * \param category Value to be passed in to the category field
     * \param severity Value to be passed in to the severity field
     * \param status Value to be passed in to the status field
     * \param agentId Value to be passed in to the agent field
     * \param ticketId Ticket to be updated
     * \return Returns true the ticket was successfully updated; false, otherwise
     */
    bool update(QString category, QString severity, QString status, QString agentId, QString ticketId);

    /**
     * Updates  the category, severity, status, and  agent id fields of the specified ticket and
     * returns true the ticket was successfully updated
     * \param category Value to be passed in to the category field
     * \param severity Value to be passed in to the severity field
     * \param status Value to be passed in to the status field
     * \param agentId Value to be passed in to the agent field
     * \param closedDate Value to be passed in to the closed date field
     * \param ticketId Ticket to be updated
     * \return Returns true the ticket was successfully updated; false, otherwise
     */
    bool update(QString category, QString severity, QString status, QString agentId, QString closedDate, QString ticketId);

    /**
     * \brief getAgentID gets agent ID from databse
     * \return agentID as QString
     */
    QString getAgentID();




private:
    /**
     * \brief _db Reference to the Frogers database.
     *
     */
    DatabaseController *_db;
};

#endif // TICKETLISTCONTROLLER_H
