#ifndef EDITTICKETVIEWCONTROLLER_H
#define EDITTICKETVIEWCONTROLLER_H

#include "Backend/DatabaseController.h"
#include "Backend/Ticket.h"
#include <QString>
#include <map>
#include <QSqlQuery>
#include <vector>
#include "Backend/Comment.h"

/**
 * \class EditTicketViewController
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief class that acts as a controller for the Edit Ticket View
 */

class EditTicketViewController
{
public:
    /**
     * \brief Constructor that generates a new EditTicketViewController
     */
    EditTicketViewController();




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

    /**
     * get the stored Ticket information
     * \return Return a Query containing the ticket information
     */
    QSqlQuery getTicketInfo();

    /**
     * \brief getComments get comments associated with specific ticket
     * \param id id of ticket to retrieve associated comments
     * \return vector of comments
     */
    std::vector<Comment> getComments(int id);
    /**
     * \brief getName gets full name of user based on id
     * \param id id used for finding user in databse
     * \return user's first name and last name
     */
    QString getName(int id);

    /**
     * \brief returnCommentVector gets comments associated with current ticket. used to prevent repetitive queries
     * \return vector of comments associated with ticket
     */
    std::vector<Comment> returnCommentVector();

    /**
     * \brief getCurrentAgent get agent assigned to ticket
     * \return agent assigned to ticket
     */
    int getCurrentAgent();
    /**
     * @brief getChanges Get changes vector from the database and return it
     * @return Vector containing changes made to a ticket.
     */
    std::vector<QString> getChanges();

    /**
     * \brief getLocation get location of ticket vector
     * \return position in vector
     */
    int getLocation();

    /***************************************************************************************************************************
     *SETTERS
     **************************************************************************************************************************/

    /**
     * Store the ticket information of the clicked ticket
     * \param id passed ticket id
     */
    void setTicketInfo(int id); // change the name for storeTicketInfo

    /**
     * \brief setCommentVector sets comment vector to new vector in case of updates
     * \param comments new vector to be stored as instance
     */
    void setCommentVector(std::vector<Comment> comments);


    /**************************************************************************************************************
     *OTHER METHODS
     *************************************************************************************************************/

    /**
     * Authenticates the passed agent id
     * \param id passed agent id
     * \return  return true if the agent id exists in the database; otherwise, false
     */
    bool authenticate(int id);

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
     * \param date Value to be passed in to the closed_at date
     * \param ticketId Ticket to be updated
     * \return Returns true the ticket was successfully updated; false, otherwise
     */
    bool update(QString category, QString severity, QString status, QString agentId, QString date, QString ticketId);

    /**
     * @brief passChanges Pass the changes to database controller
     * @param changes Changes made to the ticket
     */
    void passChanges( std::vector<QString> changes);

    /**
     * \brief incLocation increment vector location
     */
    void incLocation();
    /**
     * \brief decLocation decrement vector location
     */
    void decLocation();
    /**
     * \brief insertComment inserts a new comment into databse based on provided values
     * \param ticket tickedid associated with comment
     * \param agent agent assigned to comment
     * \param body main body of comment
     * \param time time the comment was created
     */
    void insertComment(int ticket, int agent, QString body, QDateTime time);
    /**
     * \brief deleteComment deletes comment from database
     * \param commentID ID of comment to be deleted
     */
    void deleteComment(int commentID);
    /**
     * \brief editComment edits comment in databse
     * \param commentID id of comment to modify
     * \param body modified comment body
     */
    void editComment(int commentID,QString body);
    /**
     * \brief closeTicket adds a closedat date to ticket
     * \param ticketID id of ticket to close
     */
    void closeTicket(int ticketID);
    /**
     * \brief updateTicketHistory updates history of ticket with ticket values
     * \param ticketID unique id of ticket
     * \param agent_id id of agent assigned to ticket
     * \param modified_at date of modification
     * \param description description of ticket
     */
    void updateTicketHistory(QString ticketID, QString agent_id, QString modified_at, QString description);

private:

    /*********************************************************************************************************
     *INSTANCE VARIABLES
     ********************************************************************************************************/

    /**
     * \brief _db Reference to the Frogers database.
     *
     */
    DatabaseController *_db;

    /**
      * @brief _severityMap Map for category values
      */
    std:: map <QString, int>_severityMap;

    /**
      * @brief _categoryMap Map for category values
      */
    std:: map<QString, int> _categoryMap;
    /**
      * @brief _ticketQuery query that holds a ticket information
      */
    QSqlQuery _ticketQuery;

    /**
      * @brief _comments Vector containing comments
      */
    std::vector<Comment> _comments;

    /**
      * @brief _commentLocation Current index of the comment vector
      */
    int _commentLocation;
};

#endif // EDITTICKETVIEWCONTROLLER_H
