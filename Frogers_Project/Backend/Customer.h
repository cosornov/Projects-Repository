#ifndef CUSTOMER_H
#define CUSTOMER_H

/**
 * \class Customer
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief class that wraps data for a customer object.
 */

#include "User.h"
#include <QString>

class Customer : public User
{
public:
    /**
     * \brief Customer constructor
     * \param id id of customer
     * \param firstname first name of customer
     * \param lastname last name of customer
     * \param email email of customer
     * \param password password of customer
     * \param role role of customer
     * \param phone phone number of customer
     * \param planid frogers plan id of customer
     */
    Customer(int id, QString firstname,QString lastname,QString email, QString password, QString role, QString phone, QString planid);

    /**
     * \brief getPhone getter
     * \return the phone number of the customer
     */
    QString getPhone();

    /**
     * \brief getPlanId getter
     * \return the plan id of the customer
     */
    QString getPlanId();

    /**
     * \brief setPhone setter
     * \param phone the new phone number of the customer
     */
    void setPhone(QString phone);

private:
    /**
     * \brief _phone stored phone number
     */
    QString _phone;

    /**
     * \brief _planid stored plan id
     */
    QString _planid;
};

#endif // CUSTOMER_H
