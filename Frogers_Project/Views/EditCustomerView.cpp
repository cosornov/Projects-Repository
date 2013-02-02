#include "Views/EditCustomerView.h"
#include "ui_EditCustomerView.h"
#include <QDebug>
#include <QMessageBox>

EditCustomerView::EditCustomerView(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::EditCustomerView)
{
    ui->setupUi(this);
    _controller = new EditCustomerController;

    // Make the main screen fixed size
    this->setFixedSize(this->width(), this->height());

    //Set the drop down options for subscription
    ui->subscriptionInput->addItem("Lite");
    ui->subscriptionInput->addItem("Casual Surfer");
    ui->subscriptionInput->addItem("Internet Addict");
    ui->subscriptionInput->addItem("Unlimited");

    //set up error messages
    ui->firstNameError->setStyleSheet("QLabel { color : red; }");
    ui->lastNameError->setStyleSheet("QLabel { color : red; }");
    ui->emailError->setStyleSheet("QLabel { color : red; }");
    ui->phoneError->setStyleSheet("QLabel { color : red; }");
    ui->passwordError->setStyleSheet("QLabel { color : red; }");
    ui->confirmError->setStyleSheet("QLabel { color : red; }");
    ui->firstNameError->clear();
    ui->lastNameError->clear();
    ui->emailError->clear();
    ui->passwordError->clear();
    ui->phoneError->clear();
    ui->confirmError->clear();

    //connect buttons with their slots
    connect(ui->okayButton, SIGNAL(clicked()), this, SLOT(okayClicked()));
    connect(ui->cancelButton, SIGNAL(clicked()), this, SLOT(cancelClicked()));
}

void EditCustomerView:: fillForm(){
    // fill the form with initial data
    std::vector<QString> info = this->_controller->getCustomerInfo();
    ui->firstNameInput->setText(info.at(1));
    ui->lastNameInput->setText(info.at(2));
    ui->emailInput->setText(info.at(3));
    ui->passwordInput->setText(info.at(4));
    ui->confirmInput->setText(info.at(4));
    ui->phoneInput->setText(info.at(6));
    QString sub = info.at(7);
    if (sub == "1"){
        ui->subscriptionInput->setCurrentIndex(0);
    }
    else if (sub == "2"){
        ui->subscriptionInput->setCurrentIndex(1);
    }
    else if (sub == "3"){
        ui->subscriptionInput->setCurrentIndex(2);
    }
    else{
        ui->subscriptionInput->setCurrentIndex(3);
    }
}

void EditCustomerView::cancelClicked(){
    this->close();
}

void EditCustomerView::okayClicked(){

    //customer
    Customer* c = new Customer(this->_controller->getCustomerInfo().at(0).toInt(),"","","","","","","");

    //cache all fields from user
    QString firstName = ui->firstNameInput->text();
    QString lastName = ui->lastNameInput->text();
    QString email = ui->emailInput->text();
    QString password = ui->passwordInput->text();
    QString confirm = ui->confirmInput->text();
    QString phone = ui->phoneInput->text();

    //store old password in case confirm password field does not match, but password field is not empty
    QString oldPassword = this->_controller->getCustomerInfo().at(4);

    //ensure all fields are not empty
    bool valid = true;

    if(!this->_controller->validate(firstName)){
        ui->firstNameError->setText("First Name can not be blank");
        valid=false;
    }
    else{
        c->setFirstName(firstName);
        ui->firstNameError->setText("");
    }
    if(!this->_controller->validate(lastName)){
        ui->lastNameError->setText("Last Name can not be blank");
        valid=false;
    }
    else{
        c->setLastName(lastName);
        ui->lastNameError->setText("");
    }
    if(!this->_controller->validate(email)){
        ui->emailError->setText("email can not be blank");
        valid=false;
    }
    else if (!this->_controller->validEmail(email)){
        ui->emailError->setText("invalid email");
        valid=false;
    }
    else if(this->_controller->emailExists(email,  this->_controller->getCustomerInfo().at(3))){
        ui->emailError->setText("email exists in database");
        valid=false;
    }
    else{
        c->setEmail(email);
        ui->emailError->setText("");
    }
    if(!this->_controller->validate(password)){
        ui->passwordError->setText("password can not be blank");
        valid=false;
    }
    else{
        c->setPassword(password);
        ui->passwordError->setText("");
    }
    if(!this->_controller->validate(phone)){
        ui->phoneError->setText("phone can not be blank");
        valid=false;
    }
    else if (!this->_controller->validPhone(phone)){
        ui->phoneError->setText("invalid phone number");
        valid=false;
    }
    else{
        c->setPhone(phone);
        ui->phoneError->setText("");
    }
    if(confirm!=password){
        ui->confirmError->setText("passwords do not match");
        c->setPassword(oldPassword);
        valid=false;
    }
    else
        ui->confirmError->setText("");

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

            this->_controller->saveCustomer(c);
            //close the window
            this->hide();
            break;
        case QMessageBox::Cancel:
            // Cancel was clicked
            break;
        default:
            // should never be reached
            break;
        }
    }

}

EditCustomerView::~EditCustomerView()
{
    delete ui;
}

EditCustomerController* EditCustomerView::getController(){
    return _controller;
}
