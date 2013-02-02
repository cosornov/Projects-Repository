#include "EditTicketView.h"
#include "ui_EditTicketView.h"

#include <QMessageBox>
#include <QString>
#include <map>
#include <QDebug>
#include <iostream>
#include <Backend/Comment.h>

using std::map;
using std :: endl;


EditTicketView::EditTicketView(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::EditTicketView)
{
    ui->setupUi(this);
    //make the  screen fixed size
    this->setFixedSize(this->width(), this->height());

    ui->categoryError->setStyleSheet("QLabel {color:red};");
    ui->severityError->setStyleSheet("QLabel {color:red};");
    ui->userError->setStyleSheet("QLabel {color:red};");
    ui->addCommnetError->setStyleSheet("QLabel {color:red};");
    ui->bodyError->setStyleSheet("QLabel {color:red};");

    //hide error labels
    this->clear();


    this->_controller =  new EditTicketViewController(); // initialize controller
    //fill combo boxes

    //missing : display the index of the selected ticket for both combo boxes

    //this->_controller = EditTicketViewController();
    map<QString, int> severityMap = _controller->getSeverityMap();
    map<QString, int> categoryMap = _controller->getCategoryMap();


    // load the severity combo box
    for(map<QString, int> :: iterator s_it = severityMap.begin(); s_it!=severityMap.end(); ++s_it)
    {
        ui->severityCombo->addItem(s_it->first,s_it->second);

    }


    //load category combo box
    for(map<QString, int> :: iterator c_it = categoryMap.begin(); c_it!=categoryMap.end(); ++c_it)
    {
        ui->categoryCombo->addItem(c_it->first,c_it->second);
    }

    //load the status combo box
    ui->statusDropDown->addItem("Open");
    ui->statusDropDown->addItem("Pending");
    ui->statusDropDown->addItem("Closed");

    //connect buttons with their slots
    connect(ui->saveCancelButton,SIGNAL(accepted()),this,SLOT(saveClicked()));
    connect(ui->saveCancelButton,SIGNAL(rejected()),this,SLOT (cancelClicked()));


    connect(ui->nextButton, SIGNAL(clicked()), this, SLOT(nextClicked()));
    connect(ui->prevButton, SIGNAL(clicked()), this, SLOT(prevClicked()));
    connect(ui->deleteButton, SIGNAL(clicked()), this, SLOT(deleteClicked()));
    connect(ui->addButton, SIGNAL(clicked()), this, SLOT(addClicked()));
    connect(ui->editButton,SIGNAL(clicked()),this,SLOT(editClicked()));

}

EditTicketView::~EditTicketView()
{
    delete ui;
}


void EditTicketView::nextClicked(){

    int size = this->_controller->returnCommentVector().size();
    if (size > 0){
    int current = this->_controller->getLocation();
    int end = this->_controller->returnCommentVector().size();
    if ((end-current) != 1){
        this->_controller->incLocation();
        current++;
        int newID = this->_controller->returnCommentVector().at(current).getuserID();
        this->ui->authorLabel->setText("Author: " + this->_controller->getName(newID));
        this->ui->dateLabel->setText("Date: " + this->_controller->returnCommentVector().at(current).getCreatedAt().toString());
        this->ui->bodyInput->setText(this->_controller->returnCommentVector().at(current).getBody());
        }
    }
}

void EditTicketView::prevClicked(){

    int size = this->_controller->returnCommentVector().size();
    if (size > 0){
    int current = this->_controller->getLocation();
    if (current != 0){
        this->_controller->decLocation();
        current--;
        int newID = this->_controller->returnCommentVector().at(current).getuserID();
        this->ui->authorLabel->setText("Author: " + this->_controller->getName(newID));
        this->ui->dateLabel->setText("Date: " + this->_controller->returnCommentVector().at(current).getCreatedAt().toString());
        this->ui->bodyInput->setText(this->_controller->returnCommentVector().at(current).getBody());
        }
    }
}

void EditTicketView::deleteClicked(){
    if(this->_controller->returnCommentVector().size()>0){
        int id = this->_controller->returnCommentVector().at(this->_controller->getLocation()).getId();
        this->_controller->deleteComment(id);
        this->updateComments();
    }
}

void EditTicketView::editClicked(){
    ui->bodyError->clear();
    if(ui->authorLabel->text().compare("Author: ")==0)
        ui->bodyError->setText("Comment does not exist");
    else if(ui->bodyInput->toPlainText().compare("")==0)
        ui->bodyError->setText("Comment body can not be empty");
    else{
        int commentID = this->_controller->returnCommentVector().at(this->_controller->getLocation()).getId();
        QString body = ui->bodyInput->toPlainText();
        this->_controller->editComment(commentID,body);
    }
}

void EditTicketView::addClicked(){
    ui->addCommnetError->clear();
    if(ui->addInput->text().length()!=0){
        int ticketID = this->_ticketId;
        int agentID = this->_controller->getCurrentAgent();
        QString body = this->ui->addInput->text();
        QDateTime time = QDateTime::currentDateTime();
        this->_controller->insertComment(ticketID, agentID, body, time);
        this->ui->addInput->setText("");
        this->updateComments();
        this->clear();
    }
    else{
        ui->addCommnetError->setText("Comment can not be blank!");
    }
}

void EditTicketView::saveClicked()
{

    this->clear();

    int categoryInt = ui->categoryCombo->itemData(ui->categoryCombo->currentIndex()).toInt();
    int severityInt = ui->severityCombo->itemData(ui->severityCombo->currentIndex()).toInt();
    int ticketId = this->_controller->getTicketInfo().value(0).toInt(); // get the id of the selected ticket



    // check there are no empty fields
    if(this->check() == true)
    {
        if(this->_controller->authenticate(ui->agentIdInput->text().toInt()))
        {
                if(this->_controller->update(categoryInt,severityInt,ui->statusDropDown->currentText(),ui->agentIdInput->text().toInt(), ticketId))
                {
                    QMessageBox::information(this, "Info", " the ticket has been updated");
                    if(ui->statusDropDown->currentIndex()==2){
                        this->_controller->closeTicket(ticketId);
                    }
                    this->close();
                }
        }
        else
        {
            ui->userError->setText("The agent ID: " +ui->agentIdInput->text()+ " does not exist");
        }
    }
}

void EditTicketView::cancelClicked()
{
    this->close();
}
void EditTicketView::clear()
{
    ui->userError->clear();
    ui->categoryError->clear();
    ui->severityError->clear();
    ui->bodyError->clear();
    ui->addCommnetError->clear();
}
bool EditTicketView::check()
{
    bool pass = true;

    if(ui->agentIdInput->text()== "")
    {
        ui->userError->setText("*required field");
        pass = false;
    }


    return pass;
}

void EditTicketView::fillForm()
{
    QSqlQuery query = this->_controller->getTicketInfo();
    query.next();



    QString ticketId = query.value(0).toString();
    this->_ticketId = ticketId.toInt();
    QString cusFname = query.value(1).toString();
    QString cusLname = query.value(2).toString();
    int cId = query.value(3).toInt(); // category id
    int sId = query.value(4).toInt(); // severity id
    QString status = query.value(5).toString();
    QString subject = query.value(6).toString();
    QString des = query.value(7).toString(); // description
    QString agent = query.value(8).toString();
    QDateTime openDate = query.value(9).toDateTime();
    QDateTime closeDate = query.value(10).toDateTime();




    ui->TicketNum->setText(ticketId);
    ui->cusName->setText(cusFname + " " + cusLname);
    ui->description->setPlainText(des);
    ui->subject->setText(subject);
    ui->openDate->setText(openDate.toString());
    ui->closeDate->setText(closeDate.toString());
    ui->agentLabel->setText(this->_controller->getName(agent.toInt()));
    if(status.compare("Open")==0)
        ui->statusDropDown->setCurrentIndex(0);
    if(status.compare("Pending")==0)
        ui->statusDropDown->setCurrentIndex(1);
    if(status.compare("Closed")==0)
        ui->statusDropDown->setCurrentIndex(2);


    ui->agentIdInput->setText(agent);

    std::vector<Comment> comments = this->_controller->getComments(ticketId.toInt());

    if (comments.size() > 0){
    QString author = this->_controller->getName(comments.at(0).getuserID());
    ui->authorLabel->setText("Author: "+ author);
    ui->dateLabel->setText("Date: " + comments.at(0).getCreatedAt().toString());
    ui->bodyInput->setText(comments.at(0).getBody());
    }
    // set the index of caegory combo box
    ui->categoryCombo->setCurrentIndex(cId-1); // -1 because the combo box index starts at 0

    // set the index of severity combo box
    if(sId == 3)
        ui->severityCombo->setCurrentIndex(0);
    else
        ui->severityCombo->setCurrentIndex(sId);
}

void EditTicketView::updateComments(){

    this->_controller->setCommentVector(this->_controller->getComments(this->_ticketId));
    std::vector<Comment> comments = this->_controller->returnCommentVector();
    if (comments.size() > 0){
        int location = this->_controller->getLocation();
        qDebug()<<"loc: "+QString::number(location)+"    size: "+ QString::number(comments.size());
        if(location>=comments.size()){
            this->_controller->decLocation();
            location--;
        }
        QString author = this->_controller->getName(comments.at(location).getuserID());
        ui->authorLabel->setText("Author: "+ author);
        ui->dateLabel->setText("Date: " + comments.at(location).getCreatedAt().toString());
        ui->bodyInput->setText(comments.at(location).getBody());
    }
    else{
        ui->authorLabel->setText("Author: ");
        ui->dateLabel->setText("Date: ");
        ui->bodyInput->setText("");
    }
}



EditTicketViewController* EditTicketView::getController()
{
    return _controller;
}
