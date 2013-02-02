#ifndef DATABASECONTROLLER_H
#define DATABASECONTROLLER_H

#include <QSqlDatabase>
#include <Backend/DatabaseObject.h>
#include <Backend/Ticket.h>
#include <QSqlQuery>
#include "Backend/Agent.h"
#include "Backend/Customer.h"
#include "Backend/Comment.h"
#include <map>
#include <vector>
#include <QStack>


/**
 *\class DatabaseController
 *\author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief  The DatabaseController class This class acts as a static database connection that is persistant accross all views.
 *
 * @note static object to hold the database
 *     Additional Sources Used:
 *     http://www.codeproject.com/Articles/1921/Singleton-Pattern-its-implementation-with-C
 */
class DatabaseController
{
    public:

        /*******************************************************
         * Constructor/Destructor                              *
         ******************************************************/
         /**
          *\brief getInstance Returns the instance of the database
         * \return the static instance of the persistant database connection
         */
        static DatabaseController* getInstance();

        /**
         * \brief destructor
         */
        ~DatabaseController()
        {
          instanceFlag = false;
        }

        /*******************************************************
         * GETTERS                                             *
         ******************************************************/
        /**
         * \brief getDataBase getter
         * \return the database
         */
        QSqlDatabase getDataBase();

        /**
         * \brief Get the customer count from the database and return it
         * \return Returns the customer count
         */
        int getCustomerCount();

        /**
         * \ brief Get the ticket count from the database and return it
         * \return Returns the ticket count
         */
        int getTicketCount();

        /**
         * \brief Get the current signed in agent
         * \return  Returns the id of  the current signed in agent
         */
        int getAgent();

        /**
         * \brief Get categories and return them as Map
         * \return Returns Map containing the categories values
         */
        std::map<QString,int> getCategoryMap();

        /**
         * \brief Get a ticket information from the database based on the specified id
         * \param id specified ticket id
         * \return Returns a query containing the ticket information
         */
        QSqlQuery getTicketById(int id);

        /**
         * \brief getCustomerByID getter
         * \param id the id of the customer
         * \return the query containing the customer
         */
        QSqlQuery getCustomerByID(int id);

        /**
         * \brief getFullNameByID getter
         * \param id the id of the user
         * \return the first and last name of a user
         */
        QString getFullNameByID(int id);

        /**
         * \brief getTicketListData getter
         * \param currentIndex the start of the index
         * \param offset the length of the query
         * \return a query for populating the ticket list table with specified length
         */
        QSqlQuery getTicketListData(QString currentIndex, QString offset);

        /**
         * \brief getCustomerListData getter
         * \param currentIndex the start of the index
         * \param offset the length of the query
         * \return a query for populating the customer list table with specified length
         */
        QSqlQuery getCustomerListData(QString currentIndex, QString offset);

        /**
         * \brief getDatabaseObjectgetter
         * \param object database object
         * \return query of first 10 object of type of object
         */
        QSqlQuery getDatabaseObject(DatabaseObject& object);

        /**
         * \brief Get the Severities from the database and return them in a query
         * \return Returns A query conatining all the severities
         */
        QSqlQuery getSeverities();

        /**
         * \brief Get the Categories from the database and return them in a query
         * \return Returns A query conatining all the categories
         */
        QSqlQuery getCategories();

        /**
         * \brief Get Severities and return them as Map
         * \return Returns Map containing the severities values
         */
        std::map<QString,int> getSeverityMap();

        /**
         * \brief getAgent get the current logged in agent
         * \param id id of the agent
         * \return agent object corresponding to id
         */
        Agent getAgent(int id);

        /**
         * \brief getLastQuery getter
         * \return the last query of the database
         */
        QString getLastQuery();

        /**
         * \brief getCountResults returns the count result
         * \param type type of list
         * \return the size of the last query of type 'type'
         */
        int getCountResults(int type);

        /**
         * @brief getAuditTrail get all versions (changes) of a ticket
         * @param ticketID ticket number
         * @return Returns a query containing all the changes made to a ticket
         */
        QSqlQuery getAuditTrail(QString ticketID);

        /**
         * \brief getComments getter
         * \param ticketID ticket id
         * \return vector of comments of the supplied ticket
         */
        std::vector<Comment> getComments(int ticketID);

        /**
         * \brief getNotes getter
         * \param ticketID the id of the ticket
         * \return a vector of notes of the given ticket
         */
        std::vector<Comment> getNotes(int ticketID);

        /**
         * \brief getAgentOfTicket getter
         * \param ticketID ticket id
         * \return the id of an agent associated with the ticket
         */
        int getAgentOfTicket(int ticketID);

        /**
         * \brief getNames Get the actual name of a category and severity
         * \param category Category
         * \param severity Severity
         * \return
         */
        std::vector<QString> getNames(QString category, QString severity);

        /**
         * \brief getChanges  Gets the vector containing the changes made to a ticket, and return it
         * \return Returns a vector containing the changes made to a ticket
         */
        std::vector<QString> getChanges();

        /**
         * \brief loadCustomersbyFilters getter
         * \param currentIndex starting index
         * \param offset length
         * \param subscription subscription of the customer
         * \return query with required filters of supplied length for the customer list table
         */
        QSqlQuery loadCustomersbyFilters(QString currentIndex, QString offset, QString subscription);

        /**
         * \brief loadTicketsbyFilters getter
         * \param currentIndex starting index
         * \param offset length
         * \param addedFilters filters to be applied
         * \param addedFilterValues filter values
         * \return a query containing the proper filtered data for the ticket list view
         */
        QSqlQuery loadTicketsbyFilters(QString currentIndex, QString offset, QString addedFilters [], QString addedFilterValues []);

        /**
         * @brief search Searches for customers
         * @param firstname customer first name
         * @param lastname customer last name
         * @param offset staring  position of the query
         * @param subscription subscription plan
         * @param index ending position of the query
         * @return Returns a query containing the matching customers
         */
        QSqlQuery search(QString firstname,QString lastname,int offset,QString subscription,int index);


        /*******************************************************
         * SETTERS                                             *
         ******************************************************/

        /**
         * \brief Set Agent to be the current signedin agent
         * \param username current signed in agent
         */
        void setAgent(QString username);


        /*******************************************************
         * DB_UPDATES                                          *
         ******************************************************/

        /**
         * \brief Insert a Ticket into the database
         * \param t1 Ticket to be inserted into the database
         * \return Returns true if the ticket was succesfully inserted into the database; otherwise, false
         */
        bool insertTicket(Ticket t1);

        /**
         * \brief Updates the specified Ticket in the database
         * \param category Value to be passed in to the category field
         * \param severity Value to be passed in to the severity field
         * \param status Value to be passed in to the status field
         * \param agentId Value to be passed in to the agent field
         * \param ticketId Ticket to be updated
         * \return Returns true the ticket was successfully updated in the database; false, otherwise
         */
        bool updateTicket( QString category,  QString severity, QString status,  QString agentId,  QString ticketId);

        /**
         * \brief Updates the specified Ticket in the database
         * \param category Value to be passed in to the category field
         * \param severity Value to be passed in to the severity field
         * \param status Value to be passed in to the status field
         * \param agentId Value to be passed in to the agent field
         * \param Date Value to be passed in to the closed date field
         * \param ticketId Ticket to be updated
         * \return Returns true the ticket was successfully updated in the database; false, otherwise
         */
        bool updateTicket( QString category,  QString severity, QString status,  QString agentId, QString Date, QString ticketId);

        /**
         * \brief insertComment inserts comment of a ticket
         * \param ticketID ticket id associated with comment
         * \param agentID agent associated with comment
         * \param body body of comment
         * \param time time comment was listed
         */
        void insertComment(int ticketID, int agentID, QString body, QDateTime time);

        /**
         * \brief deleteComment deletes comment form a ticket
         * \param commentID unique id of the comment to be deleted
         */
        void deleteComment(int commentID);

        /**
         * \brief editComment edit body of a ticket comment
         * \param commentID unique id of the comment to be edited
         * \param body the new body of the comment
         */
        void editComment(int commentID,QString body);

        /**
         * \brief closeTicket sets a ticket as closed and applys date
         * \param ticketID unique ticket id
         */
        void closeTicket(int ticketID);

        /**
         * @brief storeChanges Store the changes made to a ticket
         * @param _changes Changes made to the ticket
         */
        void storeChanges( std::vector<QString> _changes);

        /**
         * \brief saveAgent Saves changes made to the current logged in agent
         * \param save
         */
        void saveAgent(Agent save);

        /**
         * \brief saveCustomer Saves changes made to a customer
         * \param c the customer that is being edited/update
         */
        void saveCustomer(Customer* c);

        /**
         * \brief insertNote inserts an internal note into the database
         * \param ticketID id of ticket note is associated with
         * \param agentID agent who wrote the note
         * \param body body of the message on the note
         * \param time time the note was written
         */
        void insertNote(int ticketID, int agentID, QString body, QDateTime time);

        /**
         * \brief deleteNote delete a note from a ticket
         * \param noteID unique id of note to be deleted
         */
        void deleteNote(int noteID);

        /**
         * \brief editNote edited the body of a note
         * \param noteID unique id of note to be edited
         * \param body the new message body
         */
        void editNote(int noteID, QString body);

        /**
         * @brief updateAuditList insert a new version of a ticket that has been changed
         * @param ticketID Ticket number
         * @param agent_ID Agent id
         * @param modified_at Date of the modification
         * @param description Changes made to a ticket
         */
        void updateAuditList(QString ticketID, QString agent_ID, QString modified_at, QString description);


        /*******************************************************
         * CHECKS                                              *
         ******************************************************/

        /**
         * \brief Checks if connecting to database was successful
         * \return Return true if connecting to database was successful; otherwise, false
         */
        bool is_connected();

        /**
         * \brief emailExists check if email exists in database already
         * \param email email to be checked
         * \return true if exists, false if not
         */
        bool emailExists(QString email);

        /**
         * \brief userExists checks if username is already taken by another agent
         * \param username name to be checked
         * \return true if exists, false if not
         */
        bool userExists(QString username);

    private:

        /**
         * \brief instanceFlag
         */
        static bool instanceFlag;


        /**
         * \brief _controller
         */
        static DatabaseController *_controller;

        /**
         * \brief DatabaseController constructor
         */
        DatabaseController();


        /**
         * \brief _db Reference to the Frogers database.
         *
         */
        QSqlDatabase _db;

        /**
         * \brief _agentId current agent logged in
         */
        int _agentId;


        /**
         * @brief _connectedFlag Checks if connecting to the database was successfull
         */
        bool _connectedFlag;

        /**
         * @brief _changes stores changes made to a ticket
         */
        std::vector<QString> _changes;


        /**
         * @brief _countResults
         */
        int _countResults;

        /**
         * \brief _lastQuery stored last query
         */
        QString _lastQuery = "";

        /**
         * \brief _lastCustomerListQuery stored last customer list query
         */
        QString _lastCustomerListQuery = "";

};
#endif // DATABASECONTROLLER_H
