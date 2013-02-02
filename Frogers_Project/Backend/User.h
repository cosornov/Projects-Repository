#ifndef USER_H
#define USER_H

/**
 * \class User
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief class that wraps data for a generic user object (agent or customer).
 */

#include "DatabaseObject.h"
#include <QString>


class User : public DatabaseObject
{
public:
    /**
     * \brief User constructor
     * \param id unique id of user
     * \param firstname first name of user
     * \param lastname last name of user
     * \param email email of user
     * \param password password of user
     * \param role role within Frogers company (agent, customer)
     */
    User(int id, QString firstname,QString lastname,QString email,QString password, QString role);

    /**
     * \brief getFirstName getter
     * \return the first name of the user
     */
    const QString getFirstName();

    /**
     * \brief getLastName getter
     * \return the last name of the user
     */
    const QString getLastName();

    /**
     * \brief getEmail getter
     * \return the email of the user
     */
    const QString getEmail();

    /**
     * \brief getPassword getter
     * \return the password of the user
     */
    const QString getPassword();

    /**
     * \brief getRole getter
     * \return the role of the user
     */
    const QString getRole();

    /**
     * \brief setFirstName setter
     * \param firstName the first name of the user
     */
    void setFirstName(QString firstName);

    /**
     * \brief setLastName setter
     * \param lastName the last name of the user
     */
    void setLastName(QString lastName);

    /**
     * \brief setEmail setter
     * \param email the email of the user
     */
    void setEmail(QString email);

    /**
     * \brief setPassword setter
     * \param password the password of the user
     */
    void setPassword(QString password);

    /**
     * \brief setRole setter
     * \param role the role of the user
     */
    void setRole(QString role);

protected:
    /**
     * \brief _firstname stored first name
     */
    QString _firstname;

    /**
     * \brief _lastname stored last name
     */
    QString _lastname;

    /**
     * \brief _email stored email
     */
    QString _email;

    /**
     * \brief _password stored password
     */
    QString _password;

    /**
     * \brief _role stored role
     */
    QString _role;
};

#endif // USER_H
