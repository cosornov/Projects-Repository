#include "EditTicketViewController.h"
#include "Backend/DatabaseController.h"
#include "QSqlQuery"
#include <QString>
#include <QDebug>
#include <QDateTime>

using std::map;
using std::vector;


EditTicketViewController::EditTicketViewController()
{
    this->_db = DatabaseController::getInstance();
    this->_commentLocation = 0;
}
bool EditTicketViewController::authenticate(int id)
{
    QString idString =  QString::number(id); // covert id into string
    QSqlQuery query(this->_db->getDataBase());
    query.prepare("SELECT id FROM users WHERE id = :idString and role='agent' ");
    query.bindValue(":idString", idString);
    if (!query.exec())
    {
        qFatal("Unable to execute query.");
        return false;
    }
    query.next();
    if( query.value(0).toInt() !=0 && query.value(0).toInt() == id) // 0 means that the query is empty
        return true;
    else
        return false;
}
bool EditTicketViewController::update(QString category, QString severity, QString status, QString agentId, QString ticketId)
{
    return this->_db->updateTicket(category, severity, status, agentId,  ticketId);
}
bool EditTicketViewController::update(QString category, QString severity, QString status, QString agentId, QString date, QString ticketId)
{
    return this->_db->updateTicket(category, severity, status, agentId, date, ticketId);
}

void EditTicketViewController::closeTicket(int ticketID){
    this->_db->closeTicket(ticketID);
}

void EditTicketViewController::updateTicketHistory(QString ticketid, QString agent_id, QString modified_at, QString description){
    this->_db->updateAuditList(ticketid, agent_id, modified_at, description);
}

map<QString,int>  EditTicketViewController::getSeverityMap()
{
    _severityMap = this->_db->getSeverityMap();

    return _severityMap;
}
map<QString,int> EditTicketViewController::getCategoryMap()
{


    _categoryMap = this->_db->getCategoryMap();

    return _categoryMap;
}

void EditTicketViewController::setTicketInfo(int id)
{
   this->_ticketQuery = this->_db->getTicketById(id);

}

QSqlQuery EditTicketViewController::getTicketInfo()
{

   return this->_ticketQuery;
}

std::vector<Comment> EditTicketViewController::getComments(int id){
    this->_comments = _db->getComments(id);
    return this->_db->getComments(id);
}

int EditTicketViewController::getLocation(){
    return this->_commentLocation;
}

void EditTicketViewController::incLocation(){
    this->_commentLocation++;
}

void EditTicketViewController::decLocation(){
    this->_commentLocation--;
}

std::vector<Comment> EditTicketViewController::returnCommentVector(){
    return this->_comments;
}

void EditTicketViewController::setCommentVector(std::vector<Comment> comments){
    this->_comments=comments;

}

QString EditTicketViewController::getName(int id){
    return this->_db->getFullNameByID(id);
}

int EditTicketViewController::getCurrentAgent(){
    return this->_db->getAgent();
}

void EditTicketViewController::insertComment(int ticket, int agent, QString body, QDateTime time){
    this->_db->insertComment(ticket,agent,body,time);
}

void EditTicketViewController::deleteComment(int commentID){
    this->_db->deleteComment(commentID);
}

void EditTicketViewController::editComment(int commentID,QString body){
    this->_db->editComment(commentID,body);
}
vector<QString> EditTicketViewController::getChanges()
 {

     return  this->_db->getChanges();

 }

void EditTicketViewController::passChanges( vector<QString> changes)
{
    this->_db->storeChanges(changes);
}
