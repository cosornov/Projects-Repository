#ifndef EDITAGENTCONTROLLER_H
#define EDITAGENTCONTROLLER_H

#include "Backend/DatabaseController.h"
#include "Backend/Agent.h"

/**
 * \class EditAgentController
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief class that acts as a controller for profile modification
 */

class EditAgentController
{
public:
    /**
     * \brief Constructor that generates a new EditAgentController
     */
    EditAgentController();

    /**
     * \brief validate
     * \param test
     * \return
     */
    bool validate(QString test);

    /**
     * \brief save saves passed agent to database
     * \param save agent to save
     */
    void save(Agent save);

    /**
     * \brief getCurrentAgent gets the information of currently logged in agent
     * \return logged in agent
     */
    Agent getCurrentAgent();

    /**
     * \brief emailExists checks if email already exists in database
     * \param email email to check for in database
     * \return true if exists
     */
    bool emailExists(QString email);
    /**
     * \brief userExists checks if user exists in database
     * \param username username to check in database
     * \return true if exists
     */
    bool userExists(QString username);
    /**
     * \brief validEmail ensures that email is a valid format
     * \param email email to check
     * \return true if valid format
     */
    bool validEmail(QString email);

private:

    /**
     * \brief _db Reference to the Frogers database.
     *
     */
    DatabaseController *_db;

};
#endif // EDITAGENTCONTROLLER_H
