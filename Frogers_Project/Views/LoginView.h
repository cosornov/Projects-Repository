#ifndef LOGINVIEW_H
#define LOGINVIEW_H

/**
 * \class LoginView
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief Represents the Login View Window.
 *
 * The initial screen shown to the user upon launching the
 * application. Validates by username and password before
 * allowing the user (agent) to access the application.
 *
 */

//includes
#include "Controllers/LoginController.h"
#include <QDialog>
#include <QSqlDatabase>

namespace Ui {
class LoginView;
}

class LoginView : public QDialog
{
    Q_OBJECT
    
public:
    /**
     * Contructor for the login window.
     *
     * \param parent QWidget
     */
    explicit LoginView(QWidget *parent = 0);

    /**
     * Destructor for the login window.
     */
    ~LoginView();

public slots:
    /**
     * Actions performed once the login button is clicked. Opens main dash if
     * login infromation is valid.
     */
    void loginClicked();

    /**
     * Actions performed once the cancel button is clicked. Program exits.
     */
    void cancelClicked();

    /**
     * \brief check check for empty fields
     * \return true if valid, false if not
     */
    bool check();

    /**
     * \brief clear clear error fields
     */
    void clear();
private:

    /**************************************************************************
    * Instance Variables
    **************************************************************************/

    /*
     * The ui.
     */
    Ui::LoginView *ui;

    /**
     * \brief _controller reference to personal controller
     */
    LoginController _controller;

    /**
     * \brief _loginFails number of login failed attempts
     */
    int _loginFails;
};

#endif // LOGINVIEW_H
