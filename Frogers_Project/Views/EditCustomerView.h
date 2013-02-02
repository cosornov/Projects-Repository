#ifndef EDITCUSTOMERVIEW_H
#define EDITCUSTOMERVIEW_H

#include <QDialog>
#include "Controllers/EditCustomerController.h"

/**
 * \class EditCustomerView
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief Represents the Edit Customer Window.
 *
 * Displays a dialog with all the necessary fields needed to edit a customer's profile:
 * First name, Last name, Email, Password, Password confirmation, Phone, and Subscription plan.
 *
 */

namespace Ui {
class EditCustomerView;
}

class EditCustomerView : public QDialog
{
    Q_OBJECT
    
public:
    /**
     * \brief EditCustomerView constructor
     * \param parent
     */
    explicit EditCustomerView(QWidget *parent = 0);

    /**
     * \brief destructor
     */
    ~EditCustomerView();

    /**
     * \brief getController getter
     * \return the controller
     */
    EditCustomerController* getController();

    /**
     * \brief fillForm fills in valid fields
     */
    void fillForm();

public slots:
    /**
     * Actions performed once the okay button is clicked.
     */
    void okayClicked();

    /**
     * Actions performed once the cancel button is clicked.
     */
    void cancelClicked();
    
private:

    /**************************************************************************
    * Instance Variables
    **************************************************************************/

    /**
     * \brief ui
     */
    Ui::EditCustomerView *ui;

    /**
     * \brief _controller reference to the personal controller
     */
    EditCustomerController *_controller;
};

#endif // EDITCUSTOMERVIEW_H
