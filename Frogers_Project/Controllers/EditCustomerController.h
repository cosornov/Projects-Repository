#ifndef EDITCUSTOMERCONTROLLER_H
#define EDITCUSTOMERCONTROLLER_H

#include "Backend/DatabaseController.h"
#include "Backend/Customer.h"
#include <vector>

/**
 * \class EditCustomerController
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief class that acts as a controller for editing customer information
 */
class EditCustomerController
{
public:
    /**
     * \brief Constructor that generates a new EditCustomerController
     */
    EditCustomerController();

    /**
     * \brief InjectCustomer gets customer based on id
     * \param c customers id in database
     */
    void InjectCustomer(int c);
    /**
     * \brief getCustomerInfo gets currents customers information
     * \return stores and returns info as a vector
     */
    std::vector<QString> getCustomerInfo();

    /**
     * \brief validate ensure that test is not empty or null
     * \param test string to check
     * \return true if not empty
     */
    bool validate(QString test);
    /**
     * \brief saveCustomer saves customer to database
     * \param c pointer to customer to save
     */
    void saveCustomer(Customer* c);
    /**
     * \brief validEmail ensures that email is of a valid format
     * \param email email address to check
     * \return true if valid format
     */
    bool validEmail(QString email);
    /**
     * \brief validPhone ensures phone number is of a valid format
     * \param phone number to check
     * \return true if valid format
     */
    bool validPhone(QString phone);
    /**
    * \brief emailExists checks if email exists in database and is not equal to original email
    * \param email new email to check
    * \param oldEmail customers original email
    * \return true if email exists and old email is not the same as email
    */
    bool emailExists(QString email, QString oldEmail);

private:

    /**
     * \brief _db Reference to the Frogers database.
     *
     */
    DatabaseController *_db;

    /**
     * @brief _customer Customer Object
     */
    Customer *_customer;
};


#endif // EDITCUSTOMERCONTROLLER_H
