#ifndef NEWTICKETCONTROLLER_H
#define NEWTICKETCONTROLLER_H

#include "Backend/DatabaseController.h"
#include <QString>
#include <map>

/**
 * \class NewTicketController
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief class that acts as a controller for the NewTicketView
 */
class NewTicketController
{
public:

    /**
     * \brief Constructor that generates a new NewTicketController
     */
    NewTicketController();

    /****************************************************************************************************************
     *GETTERS
     **************************************************************************************************************/

    /**
     * Returns the severity values
     * \return Returns the severity values as a map
     */
    std::map<QString,int>  getSeverityMap();

    /**
     * Returns the category values
     * \return Returns the category values as a map
     */
    std::map<QString,int>  getCategoryMap();

    /********************************************************************************************************************
     *OTHER METHODS
     *********************************************************************************************************************/

    /**
     * Creates a new Ticket, Returns true if the ticket was successfully created
     * \param customerID Value to be inserted as customer id
     * \param category Value to be inserted as category
     * \param severity Value to be inserted as severity
     * \param subject Value to be inserted as subject
     * \param description Value to be inserted as description
     * \param created_at Value to be inserted as created_at date
     * \return Return true if the ticket was succesfully created; otherwise, false
     */
    bool createTicket(int customerID, int category, int severity, QString subject,QString description, QDateTime created_at);

    /**
     * Authenticates the passed customer id
     * \param id passed customer id
     * \return returns true if the customer id exists in the database; otherwise, false
     */
    bool authenticate(int id);

private:

    /**************************************************************************
    * Instance Variables
    **************************************************************************/

    /**
     * \brief _db Reference to the Frogers database.
     *
     */
    DatabaseController *_db;

    /**
     * @brief _severityMap Map for severity values
     */
    std:: map <QString, int>_severityMap;

    /**
     * @brief _categoryMap Map for category values
     */
    std:: map<QString, int> _categoryMap;
};

#endif // NEWTICKETCONTROLLER_H
