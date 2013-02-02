#include "NoteController.h"
#include <QDebug>

NoteController::NoteController()
{
    this->_db = DatabaseController::getInstance();
    this->_location = 0;
}


QString NoteController::getName(int id){
    return this->_db->getFullNameByID(id);
}

void NoteController::insertData(int ticketID, int agentID){
    this->_ticketID = ticketID;
    this->_agentID = agentID;
}

std::vector<Comment> NoteController::returnNoteVector(){
    return this->_notes;
}

int NoteController::getCurrentAgent(){
    return this->_agentID;
}

int NoteController::getCurrentTicket(){
    return this->_ticketID;
}

int NoteController::getLocation(){
    return this->_location;
}

void NoteController::incLocation(){
    this->_location++;
}

void NoteController::decLocation(){
    this->_location--;
}

void NoteController::insertNote(int ticket, int agent, QString body, QDateTime time){
    this->_db->insertNote(ticket, agent, body, time);
}

void NoteController::deleteNote(int commentID){
    this->_db->deleteNote(commentID);
}

void NoteController::editNote(int commentID,QString body){
    this->_db->editNote(commentID, body);
}

std::vector<Comment> NoteController::getNotes(int id){
    this->_notes = _db->getNotes(id);
    return this->_db->getNotes(id);
}

int NoteController::getAgentOfTicket(int ticketID){
    int query = this->_db->getAgentOfTicket(ticketID);
    return query;
}
