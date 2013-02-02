#include "NoteView.h"
#include "ui_NoteView.h"
#include "Controllers/NoteController.h"
#include <QFile>
#include <QString>
#include <iostream>
#include <QDebug>
#include <QMessageBox>

NoteView::NoteView(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::NoteView)
{
    //initialize controller
    ui->setupUi(this);
    this->_controller = new NoteController();

    //prepare green button
    QFile data(":/new/prefix1/greenButton.qss");
    data.open(QFile::ReadOnly);
    QTextStream styleIn(&data);
    this->_greenButton = styleIn.readAll();
    data.close();

    //fix screen size
    this->setFixedSize(this->width(), this->height());

    //setup error fields
    ui->addError->setStyleSheet("QLabel {color:red};");
    ui->bodyError->setStyleSheet("QLabel {color:red};");
    clear();

    //connect signals and slots
    connect(ui->doneButton, SIGNAL(clicked()), this, SLOT(doneClicked()));
    connect(ui->nextButton, SIGNAL(clicked()), this, SLOT(nextClicked()));
    connect(ui->prevButton, SIGNAL(clicked()), this, SLOT(prevClicked()));
    connect(ui->deleteButton, SIGNAL(clicked()), this, SLOT(deleteClicked()));
    connect(ui->addButton, SIGNAL(clicked()), this, SLOT(addClicked()));
    connect(ui->editButton,SIGNAL(clicked()),this,SLOT(editClicked()));
}

NoteView::~NoteView()
{
    delete ui;
}

void NoteView::clear(){
    //clear fields
    this->ui->addError->clear();
    this->ui->bodyError->clear();
}

void NoteView::nextClicked(){
    int size = this->_controller->returnNoteVector().size();
    if (size > 0){
    int current = this->_controller->getLocation();
    int end = this->_controller->returnNoteVector().size();
    if ((end-current) != 1){
        this->_controller->incLocation();
        current++;
        int newID = this->_controller->returnNoteVector().at(current).getuserID();
        this->ui->authorLabel->setText("Author: " + this->_controller->getName(newID));
        this->ui->dateLabel->setText("Date: " + this->_controller->returnNoteVector().at(current).getCreatedAt().toString());
        this->ui->bodyInput->setText(this->_controller->returnNoteVector().at(current).getBody());
        }
    }
    updateNotes();
}

void NoteView::prevClicked(){
    int size = this->_controller->returnNoteVector().size();
    if (size > 0){
    int current = this->_controller->getLocation();
    if (current != 0){
        this->_controller->decLocation();
        current--;
        int newID = this->_controller->returnNoteVector().at(current).getuserID();
        this->ui->authorLabel->setText("Author: " + this->_controller->getName(newID));
        this->ui->dateLabel->setText("Date: " + this->_controller->returnNoteVector().at(current).getCreatedAt().toString());
        this->ui->bodyInput->setText(this->_controller->returnNoteVector().at(current).getBody());
        }
    }
    updateNotes();
}

void NoteView::deleteClicked(){
    if(this->_controller->returnNoteVector().size()>0){
        int id = this->_controller->returnNoteVector().at(this->_controller->getLocation()).getId();
        this->_controller->deleteNote(id);
        this->updateNotes();
    }
}

void NoteView::addClicked(){

    ui->addError->clear();
    if(ui->addInput->text().length()!=0){
        int ticketID = this->_controller->getCurrentTicket();
        int agentID = this->_controller->getCurrentAgent();
        QString body = this->ui->addInput->text();
        QDateTime time = QDateTime::currentDateTime();
        this->_controller->insertNote(ticketID, agentID, body, time);
        this->ui->addInput->setText("");
        this->updateNotes();
        this->clear();
    }
    else{
        ui->addError->setText("Comment can not be blank!");
    }

}

void NoteView::editClicked(){
    ui->bodyError->clear();
    if(ui->authorLabel->text().compare("Author: ")==0)
        ui->bodyError->setText("Note does not exist");
    else if(ui->bodyInput->toPlainText().compare("")==0)
        ui->bodyError->setText("Note body can not be empty");
    else{
        int commentID = this->_controller->returnNoteVector().at(this->_controller->getLocation()).getId();
        QString body = ui->bodyInput->toPlainText();
        this->_controller->editNote(commentID, body);
        QMessageBox::information(this, "Info", "The note body has been saved.");
    }
    updateNotes();
}

void NoteView::doneClicked(){
    this->close();
}

//helper methods

void NoteView::insertData(int ticketID, int agentID){
    this->_controller->insertData(ticketID, agentID);
    fillForm();

    int ticketsAgent = this->_controller->getAgentOfTicket(this->_controller->getCurrentTicket());
    if (this->_controller->getCurrentAgent() != ticketsAgent){
        this->ui->addButton->setEnabled(false);
        ui->addButton->setStyleSheet("QPushButton { background: grey; }");
        this->ui->editButton->setEnabled(false);
        ui->editButton->setStyleSheet("QPushButton { background: grey; }");
        this->ui->deleteButton->setEnabled(false);
        ui->deleteButton->setStyleSheet("QPushButton { background: grey; }");
    }
}

void NoteView::updateNotes(){
    //get the notes vector
    this->_controller->getNotes(this->_controller->getCurrentTicket());
    std::vector<Comment> notes = this->_controller->returnNoteVector();
    //if vector has size
    if (notes.size() > 0){
        int location = this->_controller->getLocation();
        if(location>=notes.size()){
            this->_controller->decLocation();
            location--;
        }
        QString author = this->_controller->getName(notes.at(location).getuserID());
        ui->authorLabel->setText("Author: "+ author);
        ui->dateLabel->setText("Date: " + notes.at(location).getCreatedAt().toString());
        ui->bodyInput->setText(notes.at(location).getBody());
    }
    else{
        ui->authorLabel->setText("Author: ");
        ui->dateLabel->setText("Date: ");
        ui->bodyInput->setText("");
    }

    // BUTTON CHECKS for enable and disable
    if (this->_controller->returnNoteVector().size() == 0){
        this->ui->bodyInput->setEnabled(false);

        this->ui->nextButton->setEnabled(false);
        ui->nextButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->prevButton->setEnabled(false);
        ui->prevButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->editButton->setEnabled(false);
        ui->editButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->deleteButton->setEnabled(false);
        ui->deleteButton->setStyleSheet("QPushButton { background: grey; }");
    }
    else if (this->_controller->returnNoteVector().size() == 1){
        this->ui->bodyInput->setEnabled(true);

        this->ui->nextButton->setEnabled(false);
        ui->nextButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->prevButton->setEnabled(false);
        ui->prevButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->editButton->setEnabled(true);
        ui->editButton->setStyleSheet(this->_greenButton);

        this->ui->deleteButton->setEnabled(true);
        ui->deleteButton->setStyleSheet(this->_greenButton);
    }
    else if (this->_controller->getLocation() == 0 && (this->_controller->returnNoteVector().size() > 1)){
        this->ui->bodyInput->setEnabled(true);

        this->ui->prevButton->setEnabled(false);
        ui->prevButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->nextButton->setEnabled(true);
        ui->nextButton->setStyleSheet(this->_greenButton);

        this->ui->editButton->setEnabled(true);
        ui->editButton->setStyleSheet(this->_greenButton);

        this->ui->deleteButton->setEnabled(true);
        ui->deleteButton->setStyleSheet(this->_greenButton);
    }
    else if ((this->_controller->returnNoteVector().size() > 1) && (this->_controller->returnNoteVector().size() - this->_controller->getLocation()) == 1){
        this->ui->bodyInput->setEnabled(true);

        this->ui->nextButton->setEnabled(false);
        ui->nextButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->prevButton->setEnabled(true);
        ui->prevButton->setStyleSheet(this->_greenButton);

        this->ui->editButton->setEnabled(true);
        ui->editButton->setStyleSheet(this->_greenButton);

        this->ui->deleteButton->setEnabled(true);
        ui->deleteButton->setStyleSheet(this->_greenButton);
    }
    else if ((this->_controller->returnNoteVector().size() > 1) && ((this->_controller->returnNoteVector().size() - this->_controller->getLocation()) != 1) && (this->_controller->returnNoteVector().size() != 0)){
        this->ui->bodyInput->setEnabled(true);

        this->ui->nextButton->setEnabled(true);
        ui->nextButton->setStyleSheet(this->_greenButton);

        this->ui->prevButton->setEnabled(true);
        ui->prevButton->setStyleSheet(this->_greenButton);

        this->ui->editButton->setEnabled(true);
        ui->editButton->setStyleSheet(this->_greenButton);

        this->ui->deleteButton->setEnabled(true);
        ui->deleteButton->setStyleSheet(this->_greenButton);
    }
}

void NoteView::fillForm(){
    // populate note vector
    std::vector<Comment> notes = this->_controller->getNotes(this->_controller->getCurrentTicket());

    // fill initial form
    if (notes.size() != 0){
    QString authorName = this->_controller->getName(this->_controller->returnNoteVector().at(0).getuserID());
    this->ui->authorLabel->setText("Author: " + authorName);
    QString dateString = this->_controller->returnNoteVector().at(0).getCreatedAt().toString();
    this->ui->dateLabel->setText("Date: " + dateString);
    QString bodyString = this->_controller->returnNoteVector().at(0).getBody();
    this->ui->bodyInput->setText(bodyString);
    }
    // BUTTON CHECKS
    if (this->_controller->returnNoteVector().size() == 0){
        this->ui->bodyInput->setEnabled(false);

        this->ui->nextButton->setEnabled(false);
        ui->nextButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->prevButton->setEnabled(false);
        ui->prevButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->editButton->setEnabled(false);
        ui->editButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->deleteButton->setEnabled(false);
        ui->deleteButton->setStyleSheet("QPushButton { background: grey; }");
    }
    else if (this->_controller->returnNoteVector().size() == 1){
        this->ui->bodyInput->setEnabled(true);

        this->ui->nextButton->setEnabled(false);
        ui->nextButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->prevButton->setEnabled(false);
        ui->prevButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->editButton->setEnabled(true);
        ui->editButton->setStyleSheet(this->_greenButton);

        this->ui->deleteButton->setEnabled(true);
        ui->deleteButton->setStyleSheet(this->_greenButton);
    }
    else if (this->_controller->getLocation() == 0 && (this->_controller->returnNoteVector().size() > 1)){
        this->ui->bodyInput->setEnabled(true);

        this->ui->prevButton->setEnabled(false);
        ui->prevButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->nextButton->setEnabled(true);
        ui->nextButton->setStyleSheet(this->_greenButton);
        this->ui->editButton->setEnabled(true);
        ui->editButton->setStyleSheet(this->_greenButton);
        this->ui->deleteButton->setEnabled(true);
        ui->deleteButton->setStyleSheet(this->_greenButton);
    }
    else if ((this->_controller->returnNoteVector().size() > 1) && (this->_controller->returnNoteVector().size() - this->_controller->getLocation()) == 1){
        this->ui->bodyInput->setEnabled(true);

        this->ui->nextButton->setEnabled(false);
        ui->nextButton->setStyleSheet("QPushButton { background: grey; }");

        this->ui->prevButton->setEnabled(true);
        ui->prevButton->setStyleSheet(this->_greenButton);
        this->ui->editButton->setEnabled(true);
        ui->editButton->setStyleSheet(this->_greenButton);
        this->ui->deleteButton->setEnabled(true);
        ui->deleteButton->setStyleSheet(this->_greenButton);
    }
    else if ((this->_controller->returnNoteVector().size() > 1) && ((this->_controller->returnNoteVector().size() - this->_controller->getLocation()) != 1) && (this->_controller->returnNoteVector().size() != 0)){
        this->ui->bodyInput->setEnabled(true);

        this->ui->nextButton->setEnabled(true);
        ui->nextButton->setStyleSheet(this->_greenButton);

        this->ui->prevButton->setEnabled(true);
        ui->prevButton->setStyleSheet(this->_greenButton);

        this->ui->editButton->setEnabled(true);
        ui->editButton->setStyleSheet(this->_greenButton);

        this->ui->deleteButton->setEnabled(true);
        ui->deleteButton->setStyleSheet(this->_greenButton);
    }
}

