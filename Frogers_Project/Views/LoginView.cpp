//includes
#include "Views/LoginView.h"
#include "ui_LoginView.h"
#include "Views/DashboardView.h"
#include "Controllers/LoginController.h"
#include <QSqlQuery>
#include <QTextStream>
#include <QSqlDatabase>
#include <QErrorMessage>

LoginView::LoginView(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::LoginView)
{
    ui->setupUi(this);

    this->_controller = LoginController(); // initialize controller

    //connect buttons with their slots
    connect(ui->loginButon, SIGNAL(clicked()), this, SLOT(loginClicked()));
    connect(ui->cancelButton, SIGNAL(clicked()), this, SLOT(cancelClicked()));

    //make the login screen fixed size
    this->setFixedSize(this->width(), this->height());

    //assign controller and model
    this->_controller = LoginController();

    // initialize the number of login tries
    _loginFails = 1;
    // make the error label red
    ui->userError->setStyleSheet("QLabel { color : red; }");
    ui->passError->setStyleSheet("QLabel {color:red};");
    //hide the error label
    this->clear();
}

LoginView::~LoginView()
{
    delete ui;
}

void LoginView::loginClicked(){
    this->clear(); // reset the error labels

    if(check())
    {
        if (this->_controller.authenticate(ui->userNameField->text(),ui->passwordField->text())){
        DatabaseController::getInstance()->setAgent(ui->userNameField->text()); // set the agent


        // hide login
        this->hide();

        // and create dashboard
        DashboardView *m = new DashboardView();
        m->show();


        }
        else {

             QErrorMessage errorMessage;
             errorMessage.setWindowTitle("Error");

             //invalid login info error
             errorMessage.showMessage("Invalid login information. Please check user name and/or password and try again.");
             errorMessage.exec();

            if(_loginFails >= 3)
            {
                errorMessage.showMessage("You have exceeded the maximum of login tries. Try again later.");
                 errorMessage.exec();
                this->close();
            }
            else
            {
                // increase loginFail count
                _loginFails++;
            }

        }

    }
}

void LoginView::cancelClicked(){
    this->close();
}
bool LoginView::check()
{
    bool pass = true;

    if(ui->userNameField->text()== "")
    {
        ui->userError->setText("*required field");
        pass = false;
    }
    if(ui->passwordField->text()== "")
    {
        ui->passError->setText("*required field");
        pass = false;
    }

    return pass;
}
void LoginView::clear()
{
    ui->userError->clear();
    ui->passError->clear();

}
