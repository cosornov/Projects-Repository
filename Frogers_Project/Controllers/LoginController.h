#ifndef LOGINCONTROLLER_H
#define LOGINCONTROLLER_H

#include <QString>
#include "Backend/DatabaseController.h"

/**
 * \class LoginController
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief class that acts as a controller for the LoginView
 */
class LoginController
{
public:

    /**
     * \brief Constructor that generates a new LoginController
     */
    LoginController();

    /**
     * Authenticates the login information against database records.
     *
     * \param name the name the agent is attempting to log in with
     * \param pass the password the agent is attempting to log in with
     * \return true is information is valid, false otherwise
     */
    bool authenticate(QString name, QString pass);

    /**
     * Sets the current Agent id
     * \param username current user
     */
    void setAgentId(QString username);

private:

    /**
     * \brief _db Reference to the Frogers database.
     */
    DatabaseController *_db;

};

#endif // LOGINCONTROLLER_H
