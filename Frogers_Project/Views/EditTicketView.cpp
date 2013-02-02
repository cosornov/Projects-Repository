#include "EditTicketView.h"
#include "ui_EditTicketView.h"
#include "Controllers/EditTicketViewController.h"
#include "NoteView.h"

#include <QMessageBox>
#include <QString>
#include <map>
#include <QDebug>
#include <iostream>
#include <Backend/Comment.h>
#include <vector>
#include <string>
#include <QFile>

using std::map;
using std::string;
using std::endl;
using std::vector;


EditTicketView::EditTicketView(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::EditTicketView)
{

    QFile data(":/new/prefix1/greenButton.qss");
    data.open(QFile::ReadOnly);
    QTextStream styleIn(&data);
    this->_greenButton = styleIn.readAll();
    data.close();

    ui->setupUi(this);
    //make the  screen fixed size
    this->setFixedSize(this->width(), this->height());

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
    ui->statusDropDown->addItem("Pending");
    ui->statusDropDown->addItem("Closed");

    //connect buttons with their slots
    connect(ui->saveCancelButton,SIGNAL(accepted()),this,SLOT(saveClicked()));
    connect(ui->saveCancelButton,SIGNAL(rejected()),this,SLOT (cancelClicked()));
    connect(ui->noteButton, SIGNAL(clicked()), this, SLOT(noteClicked()));


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
    updateComments();
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
    updateComments();
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
        QMessageBox::information(this, "Info", "The comment has been saved.");
    }
    this->updateComments();
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

void EditTicketView::noteClicked(){
    NoteView *m = new NoteView();

    m->insertData(this->_ticketId, this->_controller->getCurrentAgent());

//    m->show();
    m->exec();

}

void EditTicketView::saveClicked()
{
    this->clear();

    int categoryInt = ui->categoryCombo->itemData(ui->categoryCombo->currentIndex()).toInt();
    int severityInt = ui->severityCombo->itemData(ui->severityCombo->currentIndex()).toInt();
    int ticketId = this->_controller->getTicketInfo().value(0).toInt(); // get the id of the selected ticket

    QString categoryString = ui->categoryCombo->itemData(ui->categoryCombo->currentIndex()).toString();
    QString severityString = ui->severityCombo->itemData(ui->severityCombo->currentIndex()).toString();
    QString ticketIdString = this->_controller->getTicketInfo().value(0).toString();

    QString closedDate = "NULL";
    bool update;

    // Check there are no empty fields
    if(this->check() == true)
    {
        if(this->_controller->authenticate(ui->agentIdInput->text().toInt()))
        {
            if(ui->statusDropDown->currentText() == "Pending")
                update = this->_controller->update(categoryString,severityString,ui->statusDropDown->currentText(),ui->agentIdInput->text(), closedDate, ticketIdString);
            else
                update = this->_controller->update(categoryString,severityString,ui->statusDropDown->currentText(),ui->agentIdInput->text(), ticketIdString);



            if(update)
            {
                QMessageBox::information(this, "Info", "The ticket has been updated");

                vector<QString> changes = this->_controller->getChanges(); // get the changes vector

                // clear the vector
                changes.clear();

                //add save the information before any user changes. this is for the undo funtionality
                changes.push_back(_query.value(3).toString());// push the category
                changes.push_back(_query.value(4).toString()); // push the severity
                changes.push_back(_query.value(5).toString()); // push the status
                changes.push_back(_query.value(8).toString()); // push the agent
                changes.push_back(_query.value(0).toString()); // push the ticket id

                this->_controller->passChanges(changes); // pass changes

                // Update the history tab and the audit_list database table.
                // category, severity, status and agent_id

                QDateTime current = QDateTime::currentDateTime();
                QString date = current.toString("yyyy-MM-dd hh:mm:ss");
                QString description = "";

                description = QString::number(categoryInt) + "#" + QString::number(severityInt) + "#" + ui->statusDropDown->currentText() + "#" + ui->agentIdInput->text() + "#";
                this->_controller->updateTicketHistory(QString::number(ticketId), ui->agentIdInput->text(), date, description);

                if(ui->statusDropDown->currentIndex()==1){
                    this->_controller->closeTicket(ticketId);
                }
                this->close();
            }
        }
        else
        {
            ui->userError->setText("The agent ID does not exist");
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
    _query = this->_controller->getTicketInfo();
    _query.next();

    QString ticketId = _query.value(0).toString();
    this->_ticketId = ticketId.toInt();
    QString cusFname = _query.value(1).toString();
    QString cusLname = _query.value(2).toString();
    int cId = _query.value(3).toInt(); // category id
    int sId = _query.value(4).toInt(); // severity id
    QString status = _query.value(5).toString();
    QString subject = _query.value(6).toString();
    QString des = _query.value(7).toString(); // description
    QString agent = _query.value(8).toString();
    QDateTime openDate = _query.value(9).toDateTime();
    QDateTime closeDate = _query.value(10).toDateTime();

    ui->TicketNum->setText(ticketId);
    ui->cusName->setText(cusFname + " " + cusLname);
    ui->description->setPlainText(des);
    ui->subject->setText(subject);
    ui->openDate->setText(openDate.toString());
    ui->closeDate->setText(closeDate.toString());
    ui->agentLabel->setText(this->_controller->getName(agent.toInt()));
    if(status.compare("Pending")==0)
        ui->statusDropDown->setCurrentIndex(0);
    if(status.compare("Closed")==0)
        ui->statusDropDown->setCurrentIndex(1);


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

    // BUTTON CHECKS
    if (this->_controller->returnCommentVector().size() == 0){
        this->ui->bodyInput->setEnabled(false);

        //        this->ui->applyButton->setStyleSheet("QPushButton { background: grey; }");
        this->ui->nextButton->setEnabled(false);
        this->ui->nextButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->prevButton->setEnabled(false);
        this->ui->prevButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->editButton->setEnabled(false);
        this->ui->editButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->deleteButton->setEnabled(false);
        this->ui->deleteButton->setStyleSheet("QPushButton { background: grey; }");
    }
    else if (this->_controller->returnCommentVector().size() == 1){
        this->ui->bodyInput->setEnabled(true);

        this->ui->nextButton->setEnabled(false);
        this->ui->nextButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->prevButton->setEnabled(false);
        this->ui->prevButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->editButton->setEnabled(true);
        this->ui->editButton->setStyleSheet(this->_greenButton);

        this->ui->deleteButton->setEnabled(true);
        this->ui->deleteButton->setStyleSheet(this->_greenButton);
    }
    else if (this->_controller->getLocation() == 0 && (this->_controller->returnCommentVector().size() > 1)){
        this->ui->bodyInput->setEnabled(true);

        this->ui->prevButton->setEnabled(false);
        this->ui->prevButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->nextButton->setEnabled(true);
        this->ui->nextButton->setStyleSheet(this->_greenButton);

        this->ui->editButton->setEnabled(true);
        this->ui->editButton->setStyleSheet(this->_greenButton);

        this->ui->deleteButton->setEnabled(true);
        this->ui->deleteButton->setStyleSheet(this->_greenButton);
    }
    else if ((this->_controller->returnCommentVector().size() > 1) && (this->_controller->returnCommentVector().size() - this->_controller->getLocation()) == 1){
        this->ui->bodyInput->setEnabled(true);

        this->ui->nextButton->setEnabled(false);
        this->ui->nextButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->prevButton->setEnabled(true);
        this->ui->prevButton->setStyleSheet(this->_greenButton);

        this->ui->editButton->setEnabled(true);
        this->ui->editButton->setStyleSheet(this->_greenButton);

        this->ui->deleteButton->setEnabled(true);
        this->ui->editButton->setStyleSheet(this->_greenButton);
    }
    else if ((this->_controller->returnCommentVector().size() > 1) && ((this->_controller->returnCommentVector().size() - this->_controller->getLocation()) != 1) && (this->_controller->returnCommentVector().size() != 0)){
        this->ui->bodyInput->setEnabled(true);

        this->ui->nextButton->setEnabled(true);
        this->ui->nextButton->setStyleSheet(this->_greenButton);

        this->ui->prevButton->setEnabled(true);
        this->ui->prevButton->setStyleSheet(this->_greenButton);

        this->ui->editButton->setEnabled(true);
        this->ui->editButton->setStyleSheet(this->_greenButton);

        this->ui->deleteButton->setEnabled(true);
        this->ui->deleteButton->setStyleSheet(this->_greenButton);
    }
}

void EditTicketView::updateComments(){

    this->_controller->setCommentVector(this->_controller->getComments(this->_ticketId));
    std::vector<Comment> comments = this->_controller->returnCommentVector();
    if (comments.size() > 0){
        int location = this->_controller->getLocation();
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

    // BUTTON CHECKS
    if (this->_controller->returnCommentVector().size() == 0){
        this->ui->bodyInput->setEnabled(false);

        //        this->ui->applyButton->setStyleSheet("QPushButton { background: grey; }");
        this->ui->nextButton->setEnabled(false);
        this->ui->nextButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->prevButton->setEnabled(false);
        this->ui->prevButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->editButton->setEnabled(false);
        this->ui->editButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->deleteButton->setEnabled(false);
        this->ui->deleteButton->setStyleSheet("QPushButton { background: grey; }");
    }
    else if (this->_controller->returnCommentVector().size() == 1){
        this->ui->bodyInput->setEnabled(true);

        this->ui->nextButton->setEnabled(false);
        this->ui->nextButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->prevButton->setEnabled(false);
        this->ui->prevButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->editButton->setEnabled(true);
        this->ui->editButton->setStyleSheet(this->_greenButton);

        this->ui->deleteButton->setEnabled(true);
        this->ui->deleteButton->setStyleSheet(this->_greenButton);
    }
    else if (this->_controller->getLocation() == 0 && (this->_controller->returnCommentVector().size() > 1)){
        this->ui->bodyInput->setEnabled(true);

        this->ui->prevButton->setEnabled(false);
        this->ui->prevButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->nextButton->setEnabled(true);
        this->ui->nextButton->setStyleSheet(this->_greenButton);

        this->ui->editButton->setEnabled(true);
        this->ui->editButton->setStyleSheet(this->_greenButton);

        this->ui->deleteButton->setEnabled(true);
        this->ui->deleteButton->setStyleSheet(this->_greenButton);
    }
    else if ((this->_controller->returnCommentVector().size() > 1) && (this->_controller->returnCommentVector().size() - this->_controller->getLocation()) == 1){
        this->ui->bodyInput->setEnabled(true);

        this->ui->nextButton->setEnabled(false);
        this->ui->nextButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->prevButton->setEnabled(true);
        this->ui->prevButton->setStyleSheet(this->_greenButton);

        this->ui->editButton->setEnabled(true);
        this->ui->editButton->setStyleSheet(this->_greenButton);

        this->ui->deleteButton->setEnabled(true);
        this->ui->editButton->setStyleSheet(this->_greenButton);
    }
    else if ((this->_controller->returnCommentVector().size() > 1) && ((this->_controller->returnCommentVector().size() - this->_controller->getLocation()) != 1) && (this->_controller->returnCommentVector().size() != 0)){
        this->ui->bodyInput->setEnabled(true);

        this->ui->nextButton->setEnabled(true);
        this->ui->nextButton->setStyleSheet(this->_greenButton);

        this->ui->prevButton->setEnabled(true);
        this->ui->prevButton->setStyleSheet(this->_greenButton);

        this->ui->editButton->setEnabled(true);
        this->ui->editButton->setStyleSheet(this->_greenButton);

        this->ui->deleteButton->setEnabled(true);
        this->ui->deleteButton->setStyleSheet(this->_greenButton);
    }
}



EditTicketViewController* EditTicketView::getController()
{
    return _controller;
}

