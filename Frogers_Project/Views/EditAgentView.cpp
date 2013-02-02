#include "Views/EditAgentView.h"
#include "ui_EditAgentView.h"
#include <QString>
#include "Backend/Agent.h"
#include <QMessageBox>
#include <QMenu>

Agent loggedInAgent(1, "Connor","Graham","cgraha54@uwo.ca", "asdfasdf", "agent", "cgraha54");

EditAgentView::EditAgentView(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::EditAgentView)
{
    ui->setupUi(this);

    //set up controller
    this->_controller = new EditAgentController;


    //get logged in agent
    loggedInAgent=this->_controller->getCurrentAgent();

    //set up error messages
    ui->firstNameError->setStyleSheet("QLabel { color : red; }");
    ui->lastNameError->setStyleSheet("QLabel { color : red; }");
    ui->emailError->setStyleSheet("QLabel { color : red; }");
    ui->passwordError->setStyleSheet("QLabel { color : red; }");
    ui->userError->setStyleSheet("QLabel { color : red; }");
    ui->confirmError->setStyleSheet("QLabel { color : red; }");

    //clear all fiels
    this->clear();

    //populate fields with logged in agent
    this->refresh();

    //connect buttons with their slots
    connect(ui->okayButton, SIGNAL(clicked()), this, SLOT(confirmClicked()));
}

EditAgentView::~EditAgentView()
{
    delete ui;
}

void EditAgentView::confirmClicked(){

    //cache all fields from user
    QString firstName = ui->firstNameInput->text();
    QString lastName = ui->lastNameInput->text();
    QString email = ui->emailInput->text();
    QString password = ui->passwordInput->text();
    QString confirm = ui->confirmInput->text();
    QString userName = ui->userInput->text();

    //clear fields
    this->clear();

    //store old password in case confirm password field does not match, but password field is not empty
    QString oldPassword = loggedInAgent.getPassword();

    //ensure all fields are not empty
    bool valid = true;

    if(!this->_controller->validate(firstName)){
        ui->firstNameError->setText("First Name can not be blank");
        valid=false;
    }
    else{
        loggedInAgent.setFirstName(firstName);
    }
    if(!this->_controller->validate(lastName)){
        ui->lastNameError->setText("Last Name can not be blank");
        valid=false;
    }
    else{
        loggedInAgent.setLastName(lastName);
    }
    if(!this->_controller->validate(email)){
        ui->emailError->setText("email can not be blank");
        valid=false;
    }
    else if(!this->_controller->validEmail(email)){
        ui->emailError->setText("invalid email");
        valid=false;
    }
    else if(this->_controller->emailExists(email)){
        ui->emailError->setText("email exists in database");
        valid=false;
    }
    else{
        loggedInAgent.setEmail(email);
    }
    if(!this->_controller->validate(password)){
        ui->passwordError->setText("password can not be blank");
        valid=false;
    }
    else{
        loggedInAgent.setPassword(password);
    }
    if(!this->_controller->validate(userName)){
        ui->userError->setText("username can not be blank");
        valid=false;
    }
    else if(this->_controller->userExists(userName)){
        ui->userError->setText("username exists in database");
        valid=false;
    }
    else{
        loggedInAgent.setUsername(userName);
    }
    if(confirm!=password){
        ui->confirmError->setText("passwords do not match");
        loggedInAgent.setPassword(oldPassword);
        valid=false;
    }
    if(valid){

        //display confirmation message before save is preformed
        QMessageBox msgBox;
        msgBox.setText("Your profile has been modified.");
        msgBox.setInformativeText("Are you sure you wish to save your changes?");
        msgBox.setStandardButtons(QMessageBox::Save | QMessageBox::Cancel);
        msgBox.setDefaultButton(QMessageBox::Save);
        int ret = msgBox.exec();
        switch (ret) {
        case QMessageBox::Save:
            // Save was clicked
            this->_controller->save(loggedInAgent);
            break;
        case QMessageBox::Cancel:
            // Cancel was clicked
            break;
        default:
            // should never be reached
            break;
        }
    }
    //refresh values now that changes have been made
    this->refresh();
}


void EditAgentView::refresh(){

    //populate fields with logged in agents info
    ui->firstNameInput->insert(loggedInAgent.getFirstName());
    ui->lastNameInput->insert(loggedInAgent.getLastName());
    ui->emailInput->insert(loggedInAgent.getEmail());
    ui->passwordInput->insert(loggedInAgent.getPassword());
    ui->userInput->insert(loggedInAgent.getUsername());
    ui->confirmInput->insert(loggedInAgent.getPassword());
}

void EditAgentView::clear(){

    //clear error messages
    ui->firstNameError->clear();
    ui->lastNameError->clear();
    ui->emailError->clear();
    ui->passwordError->clear();
    ui->userError->clear();
    ui->confirmError->clear();

    //clear field values
    ui->firstNameInput->clear();
    ui->lastNameInput->clear();
    ui->emailInput->clear();
    ui->passwordInput->clear();
    ui->userInput->clear();
    ui->confirmInput->clear();
}




