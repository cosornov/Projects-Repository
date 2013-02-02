#include "Views/NewTicketView.h"
#include "ui_NewTicketView.h"
#include "Controllers/NewTicketController.h"

#include <QDateTime>
#include <QDebug>
#include <QMessageBox>
#include <QString>
#include <QErrorMessage>


using std::map;

NewTicketView::NewTicketView(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::NewTicketView)
{
    ui->setupUi(this);

    // make error label red
    ui->cusError->setStyleSheet("QLabel { color : red; }");
    ui->categoryError->setStyleSheet("QLabel {color:red};");
    ui->severityError->setStyleSheet("QLabel {color:red};");
    ui->subjectError->setStyleSheet("QLabel {color:red};");
    ui->desError->setStyleSheet("QLabel {color:red};");
    //ui->desError->setStyleSheet("Qlabel {color:red};");
    this->clear(); // hide the errir labels

    //combo box stuff
    ui->categoryCombo->addItem(" Choose the Category");
    ui->severityCombo->addItem(" Choose the Severity");

    this->_controller = NewTicketController();
    map<QString, int> severityMap = _controller.getSeverityMap();
    map<QString, int> categoryMap = _controller.getCategoryMap();


    // load the severity combo box
    for(map<QString, int> :: iterator s_it = severityMap.begin(); s_it!=severityMap.end(); ++s_it)
    {
        ui->severityCombo->addItem(s_it->first,s_it->second);

    }

    //load cateory combo box
    for(map<QString, int> :: iterator c_it = categoryMap.begin(); c_it!=categoryMap.end(); ++c_it)
    {
        ui->categoryCombo->addItem(c_it->first,c_it->second);
    }


    //make the  screen fixed size
    this->setFixedSize(this->width(), this->height());

    //connect buttons with their slots

    connect(ui->SaveCancelButton,SIGNAL(accepted()),this, SLOT(saveClicked()));
    connect(ui->SaveCancelButton, SIGNAL(rejected()),this,SLOT(cancelClicked()));
}

NewTicketView::~NewTicketView()
{
    delete ui;
}

void NewTicketView::saveClicked()
{
    int categoryInt = ui->categoryCombo->itemData(ui->categoryCombo->currentIndex()).toInt();
    int severityInt = ui->severityCombo->itemData(ui->severityCombo->currentIndex()).toInt();

    QDateTime startTime = QDateTime::currentDateTime(); // retrieve the current date and time


    this->clear();
    // check there are no empty fields
    if(this->check() == true)
    {
        if(this->_controller.authenticate(ui->cusIdInput->text().toInt()))
        {

            if(this->_controller.createTicket(ui->cusIdInput->text().toInt(),categoryInt,severityInt,ui->subjectInput->text(),ui->desInput->toPlainText(),startTime))
            {

                QMessageBox::information(this, "Info", "A new ticket has been created");
                this->close();
            }
        }
        else
        {
            ui->cusError->setText("The customer ID does not exist");
        }

    }


}

void NewTicketView::cancelClicked()
{
    this->close();
}
void NewTicketView::clear()
{
    ui->cusError->clear();
    ui->categoryError->clear();
    ui->severityError->clear();
    ui->subjectError->clear();
    ui->desError->clear();
}
bool NewTicketView::check()
{
    bool pass = true;
    int categoryInt = ui->categoryCombo->itemData(ui->categoryCombo->currentIndex()).toInt();
    int severityInt = ui->severityCombo->itemData(ui->severityCombo->currentIndex()).toInt();
    if(ui->cusIdInput->text()== "")
    {
        ui->cusError->setText("*required field");
        pass = false;
    }
    if(categoryInt == 0)
    {
        ui->categoryError->setText("*required field");
        pass = false;
    }
    if(severityInt == 0)
    {
        ui->severityError->setText("*required field");
        pass = false;
    }
    if(ui->subjectInput->text()== "")
    {
        ui->subjectError->setText("*required field");
        pass = false;
    }
    if(ui->desInput->toPlainText()== "")
    {
        ui->desError->setText("*required field");
        pass = false;
    }

    return pass;
}
