#ifndef AGENT_H
#define AGENT_H

/**
 * \class Agent
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief class that wraps the data needed for an Frogers agent.
 */

#include "User.h"
#include <QString>


class Agent : public User
{
public:
    /**
     * \brief Agent constructor
     * \param id id of agent
     * \param firstname first name of agent
     * \param lastname last name of agent
     * \param email email of agent
     * \param password password of agent
     * \param role of agent
     * \param username username of agent
     */
    Agent(int id, QString firstname,QString lastname,QString email, QString password, QString role, QString username);

    /**
     * \brief getUsername getter
     * \return the username of the agent
     */
    const QString getUsername();

    /**
     * \brief setUsername setter
     * \param username the username of the agent
     */
    void setUsername(QString username);

private:
    /**
     * \brief _username stored username of agent
     */
    QString _username;
};



#endif // AGENT_H
