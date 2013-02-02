#include "Backend/DatabaseController.h"
#include <Backend/Ticket.h>
#include <QSqlDatabase>
#include <QDebug>
#include <QSqlError>
#include <map>
#include <vector>

using std::map;
using std::vector;
using std::pair;

DatabaseController::DatabaseController()
{
    //create connection to database
    _db = QSqlDatabase::addDatabase("QMYSQL");
    _db.setHostName("group2.cs3307.ca");
    _db.setDatabaseName("group2");
    _db.setUserName("group2_user");
    _db.setPassword("RQFN*6Izb-E7xWH");

    if(_db.open())
    {
        _connectedFlag = true;
    }
    else
    {
        _connectedFlag = false;
    }
    this->_agentId = 0;


}

bool DatabaseController::instanceFlag = false;
DatabaseController* DatabaseController::_controller = NULL;

DatabaseController* DatabaseController::getInstance()
{
    //return instance of database as long as their is a valid connection
    if(! instanceFlag)
    {
        _controller = new DatabaseController();
        instanceFlag = true;
        return _controller;
    }
    else
    {
        return _controller;
    }
}

QSqlDatabase DatabaseController::getDataBase(){
    return this->_db;
}



void DatabaseController::updateAuditList(QString ticketID, QString agent_ID, QString modified_at, QString description) {

    QSqlQuery query(this->_controller->_db);
    query.prepare("INSERT INTO audit_trail (ticket_id, agent_id, modified_at, description) VALUES (:ticketID, :agent_ID, :modified_at, :description)");
    query.bindValue(":ticketID", ticketID);
    query.bindValue(":agent_ID", agent_ID);
    query.bindValue(":modified_at", modified_at);
    query.bindValue(":description", description);

    if (!query.exec())
    {
        qFatal("Unable to execute query.");
    }
}

QSqlQuery DatabaseController::getAuditTrail(QString ticketID) {

    QSqlQuery query(this->_controller->_db);
    query.prepare("SELECT a.id, a.ticket_id as 'Ticket ID', a.modified_at as 'Modifed at', a.description as Category FROM audit_trail a INNER JOIN tickets t ON t.id = a.ticket_id WHERE t.id = :ticketID");
    query.bindValue(":ticketID", ticketID);
    if (!query.exec())
    {
        qFatal("Unable to execute query.");
    }
    return query;
}

// Obtains the number of entries of any data set obtained through the database
int DatabaseController::getCountResults(int type){

    QString sql = "";

    if (type == 0) {
        sql = this->_lastQuery.mid(7,this->_lastQuery.length());
    } else {
        if (type == 1){
            sql = this->_lastCustomerListQuery.mid(7,this->_lastCustomerListQuery.length());
        }
    }

    sql = "SELECT COUNT(*), " + sql;

    QSqlQuery query2(this->_controller->_db);

    query2.prepare(sql);

    if (!query2.exec())
    {
        qFatal("Unable to execute query.");
    }

    query2.next();
    this->_countResults = query2.value(0).toInt();

    return this->_countResults;
}

vector<QString> DatabaseController::getNames(QString category, QString severity){

    vector<QString> temp;
    QSqlQuery query(this->_controller->_db);

    query.prepare("SELECT name FROM categories WHERE id = :category");
    query.bindValue(":category", category);
    if (!query.exec())
    {
        qFatal("Unable to execute query.");
    }

    query.next();

    temp.push_back(query.value(0).toString());

    query.prepare("SELECT name FROM severities WHERE id = :severity");
    query.bindValue(":severity", severity);

    if (!query.exec())
    {
        qFatal("Unable to execute query.");
    }

    query.next();

    temp.push_back(query.value(0).toString());

    return temp;
}

QString DatabaseController::getLastQuery(){
    return this->_lastQuery;
}

// Retrieves the number of entries currently in the table
int DatabaseController::getCustomerCount(){

    int number = 0;
    QSqlQuery query(this->_controller->_db);

    // Select number of customers specified
    query.prepare("SELECT username, COUNT(*) FROM users u INNER JOIN plans p ON u.plan_id = p.id ");

    if (!query.exec())
    {
        qFatal("Unable to execute query.");
    }

    while (query.next()){
        number = query.value(1).toInt();
    }

    return number;
}

QSqlQuery DatabaseController::getTicketById(int id){

    QSqlQuery query(this->_db);

    QString idString = QString::number(id);

    query.prepare("SELECT t.id, u.first_name,u.last_name, t.category_id , t.severity_id , t.status, t.subject, t.description, t.assigned_to_id , t.created_at, t.closed_at FROM tickets t INNER JOIN users u ON t.user_id = u.id WHERE t.id = :idString");
    query.bindValue(":idString", idString);
        if (!query.exec())
        {
            qFatal("Unable to execute query.");
        }

    return query;

}

QSqlQuery DatabaseController::getTicketListData(QString currentIndex, QString offset){
    QSqlQuery query(this->_controller->_db);

    QString agentID = QString::number(this->_agentId);

    query.prepare("SELECT t.id, u.first_name as 'Customer Fname' ,u.last_name as 'Customer Lname', c.name as Category, s.name as Severity, t.status, a.first_name as   'Agent Fname' , a.last_name as  'Agent Lname' , t.created_at, t.closed_at FROM tickets t INNER JOIN categories c ON t.category_id = c.id INNER JOIN users u ON t.user_id = u.id INNER JOIN users a ON t.assigned_to_id = a.id INNER JOIN severities s ON t.severity_id = s.id WHERE t.assigned_to_id = :agentID LIMIT :offset OFFSET :currentIndex");
    query.bindValue(":agentID", agentID);
    query.bindValue(":offset", offset);
    query.bindValue(":currentIndex", currentIndex);
    if (!query.exec())
    {
        qFatal("Unable to execute query.");
    }

    return query;
}



// Retrieves the customer list data
QSqlQuery DatabaseController::getCustomerByID(int id){

    QSqlQuery query(this->_controller->_db);

    //select
    QString theID = QString::number(id);
    query.prepare("SELECT id, first_name, last_name, email, password, role, phone, plan_id FROM users WHERE id = :id LIMIT 1");
    query.bindValue(":id", theID);
    if (!query.exec())
    {
        qFatal("Unable to execute query.");
    }
    return query;
}

// Retrieves the customer list data
QSqlQuery DatabaseController::getCustomerListData(QString currentIndex, QString offset){

    QSqlQuery query(this->_controller->_db);

    //select number of customers specified
    query.prepare("SELECT u.first_name as 'First Name', u.last_name as 'Last Name', u.email as Email, u.phone as Phone, p.name as Plan, u.ID FROM users u INNER JOIN plans p ON u.plan_id = p.id ORDER BY u.first_name LIMIT :offset OFFSET :currentIndex");
    query.bindValue(":offset", offset);
    query.bindValue(":currentIndex", currentIndex);
    if (!query.exec())
    {
        qFatal("Unable to execute query.");
    }
    return query;
}


QSqlQuery DatabaseController::getDatabaseObject(DatabaseObject& object){

    QSqlQuery query(this->_db);
    QString objectType = object.getType();

    query.prepare("SELECT * from :objectType LIMIT 10");
    query.bindValue(":objectType", objectType);

    if (!query.exec()){
        qFatal("Unable to execute query.");
    }

    return query;
}

int DatabaseController::getTicketCount()
{
    int count = 0;
    QSqlQuery query("Select COUNT(*) FROM tickets") ;
    if(!query.exec())
        return 0;
    query.next();
    count = query.value(0).toInt();
    return count;
}
bool DatabaseController::updateTicket( QString category,  QString severity, QString status,  QString agentId,  QString ticketId)
{
    QSqlQuery query (this->_db);

   query.prepare("UPDATE tickets SET category_id = :category, severity_id = :severity, status= :status, assigned_to_id= :agentId WHERE id = :ticketId");
   query.bindValue(":category", category);
   query.bindValue(":severity", severity);
   query.bindValue(":status", status);
   query.bindValue(":agentId", agentId);
   query.bindValue(":ticketId", ticketId);
   if(!query.exec())
    {
        qFatal("Unable to execute query.");
        return false;
    }

    return true;

}
bool DatabaseController::updateTicket( QString category,  QString severity, QString status,  QString agentId, QString date, QString ticketId)
{
    QSqlQuery query (this->_db);

    query.prepare("UPDATE tickets SET category_id = :category, severity_id = :severity, status= :status, assigned_to_id= :agentId, closed_at = NULL WHERE id = :ticketId");
    query.bindValue(":category", category);
    query.bindValue(":severity", severity);
    query.bindValue(":status", status);
    query.bindValue(":agentId", agentId);
    query.bindValue(":ticketId", ticketId);
    if(!query.exec())
    {
        qFatal("Unable to execute query.");
        return false;
    }

    return true;

}


void DatabaseController::closeTicket(int ticketID){
    QSqlQuery query(this->_db);

    QString idString = QString::number(ticketID);
    QDateTime current = QDateTime::currentDateTime();
    QString date = current.toString("yyyy-MM-dd hh:mm:ss");

    query.prepare("UPDATE tickets SET closed_at= :date WHERE id= :idString");
    query.bindValue(":date", date);
    query.bindValue(":idString", idString);

    if(!query.exec()){
        qFatal("Unable to execute query. saveCustomer");
    }
}

bool DatabaseController::insertTicket(Ticket t1)
{
    QSqlQuery query(this->_db);
    QString cusIdString = QString::number(t1.getUserId()); //customer id
    QString cIdString = QString::number(t1.getCategoryId()); //category id
    QString sIdString = QString::number(t1.getSeverityId()); // severity id
    QString agentIdString = QString::number(t1.getassignedToId());// current agent id
    QString time1 = t1.getCreatedAt().toString("yyyy-MM-dd hh:mm:ss");

    query.prepare("INSERT INTO tickets (user_id,category_id,severity_id,status, subject, description, assigned_to_id , created_at) Values(:cusIdString, :cIdString, :sIdString, :t1status, :t1subject, :t1desc, :agentIdString, :time1)");
    query.bindValue(":cusIdString", cusIdString);
    query.bindValue(":cIdString", cIdString);
    query.bindValue(":sIdString", sIdString);
    query.bindValue(":t1status", t1.getStatus());
    query.bindValue(":t1subject", t1.getSubject());
    query.bindValue(":t1desc", t1.getDescription());
    query.bindValue(":agentIdString", agentIdString);
    query.bindValue(":time1", time1);
    if (!query.exec()){
        qFatal("Unable to execute query.");
        return false;
    }

    return true;
}

QSqlQuery DatabaseController::loadCustomersbyFilters(QString currentIndex, QString offset, QString subscription) {

    QString agentID = QString::number(this->_agentId);

    if (subscription == "None"){
        subscription = "";
    }

    QString sql = "SELECT u.first_name as 'First Name', u.last_name as 'Last Name', u.email as Email, u.phone as Phone, p.name as Plan, u.ID FROM users u INNER JOIN plans p ON u.plan_id = p.id WHERE p.name LIKE '%" + subscription + "%' ORDER BY u.first_name ";

            if (sql != this->_lastCustomerListQuery) {
                currentIndex = "0";
            }

            this->_lastCustomerListQuery = sql;
            sql += " LIMIT " + offset + " OFFSET " + currentIndex;

            QSqlQuery query(this->_controller->_db);

            query.prepare(sql);

            if (!query.exec())
            {
                qFatal("Unable to execute query.");
            }

            return query;
}

// Method which calls all the other filters methods
QSqlQuery DatabaseController::loadTicketsbyFilters(QString currentIndex, QString offset, QString addedFilters [], QString addedFilterValues []){

    QString sql = "";

    if (addedFilterValues[6] != NULL && addedFilters[6] != NULL)
        sql = "SELECT t.id as 'Ticket ID', u.first_name as 'Cust Fname', u.last_name as 'Cust Lname', c.name as Category, s.name as Severity, t.status as 'Status', a.first_name as 'Agent Fname', a.last_name as 'Agent Lname', t.created_at as 'Created at', t.closed_at as 'Closed at', u.id FROM tickets t INNER JOIN categories c ON t.category_id = c.id INNER JOIN users u ON t.user_id = u.id INNER JOIN users a ON a.id = " + addedFilterValues[6] + " INNER JOIN severities s ON t.severity_id = s.id WHERE ";
    else
        sql = "SELECT t.id as 'Ticket ID', u.first_name as 'Cust Fname', u.last_name as 'Cust Lname', c.name as Category, s.name as Severity, t.status as 'Status', a.first_name as 'Agent Fname', a.last_name as 'Agent Lname', t.created_at as 'Created at', t.closed_at as 'Closed at', u.id FROM tickets t INNER JOIN categories c ON t.category_id = c.id INNER JOIN users u ON t.user_id = u.id LEFT OUTER JOIN users a ON a.id = t.assigned_to_id INNER JOIN severities s ON t.severity_id = s.id WHERE ";

    // Analyze the filters needed and create the SQL string
    for (int i = 0; i < 9; i++){
        if (addedFilters[i] == "Ticket Number"){
            addedFilterValues[i] = addedFilterValues[i].replace("'", "\\'");
            sql += "t.id LIKE '%" + addedFilterValues[i] + "%' AND ";
        } else {
            if (addedFilters[i] == "Customer First Name"){
                addedFilterValues[i] = addedFilterValues[i].replace("'", "\\'");
                sql += "u.first_name LIKE '%" + addedFilterValues[i] + "%' AND ";
            } else {
                if (addedFilters[i] == "Customer Last Name"){
                    addedFilterValues[i] = addedFilterValues[i].replace("'", "\\'");
                    sql += "u.last_name LIKE '%" + addedFilterValues[i] + "%' AND ";
                } else {
                    if (addedFilters[i] == "Category"){
                        addedFilterValues[i] = addedFilterValues[i].replace("'", "\\'");
                        sql += "c.name LIKE '%" + addedFilterValues[i] + "%' AND ";
                    } else {
                        if (addedFilters[i] == "Severity"){
                            addedFilterValues[i] = addedFilterValues[i].replace("'", "\\'");
                            sql += "s.name LIKE '%" + addedFilterValues[i] + "%' AND ";
                        } else {
                            if (addedFilters[i] == "Status"){
                                addedFilterValues[i] = addedFilterValues[i].replace("'", "\\'");
                                sql += "t.status LIKE '%" + addedFilterValues[i] + "%' AND ";
                            } else {
                                if (addedFilters[i] == "Assigned to"){
                                    addedFilterValues[i] = addedFilterValues[i].replace("'", "\\'");
                                    if (addedFilterValues[i] != "") {
                                        sql += "t.assigned_to_id = " + addedFilterValues[i] + " AND ";
                                    }
                                } else {
                                    if (addedFilters[i] == "Open date/time range"){
                                        addedFilterValues[i] = addedFilterValues[i].replace("'", "\\'");
                                        QString startRange = addedFilterValues[i].mid(0, addedFilterValues[i].length() - 19);
                                        QString endRange = addedFilterValues[i].mid(19, addedFilterValues[i].length());

                                        sql += "created_at between '" + startRange + "' and '" + endRange + "' AND ";
                                    } else {
                                        if (addedFilters[i] == "Closed date/time range"){
                                            addedFilterValues[i] = addedFilterValues[i].replace("'", "\\'");
                                            QString startRange = addedFilterValues[i].mid(0, addedFilterValues[i].length() - 19);
                                            QString endRange = addedFilterValues[i].mid(19, addedFilterValues[i].length());

                                            sql += "closed_at between '" + startRange + "' and '" + endRange + "' AND ";
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    QString temp = sql.mid(sql.length() - 6, sql.length());

    // Check whether there were no filters applied
    if (temp == "WHERE ") {
        sql = sql.mid(0, sql.length() - 6);
    } else
        sql = sql.mid(0,sql.length() - 4);

    if (sql != this->_lastQuery) {
        currentIndex = "0";
    }

    this->_lastQuery = sql;
    sql += " LIMIT " + offset + " OFFSET " + currentIndex;

    QSqlQuery query(this->_controller->_db);

    query.prepare(sql);

    if (!query.exec())
    {
        qFatal("Unable to execute query.");
    }

    return query;
}

//return all the severities as a map
map<QString,int>  DatabaseController::getSeverityMap()
{
    QString name;
    int id ;
    map <QString, int> severityMap;
    // get the queries from the database
    QSqlQuery sQuery(this->getSeverities());


    while(sQuery.next())
    {
        name = sQuery.value(1).toString();
        id = sQuery.value(0).toInt();
        severityMap.insert(pair<QString,int> (name, id));
    }

    return severityMap;
}
//return all the categories as a map
map<QString,int>  DatabaseController::getCategoryMap()
{
    QString name;
    int id;
    map <QString, int> categoryMap;

    // get the category query from the database
    QSqlQuery cQuery(this->getCategories());

    while(cQuery.next())
    {
        name = cQuery.value(1).toString();
        id = cQuery.value(0).toInt();
        categoryMap.insert(pair<QString,int> (name,id));
    }

    return categoryMap;
}

void DatabaseController::setAgent(QString username)
{
    QSqlQuery query (this->_db);

    query.prepare("SELECT u.id FROM users u WHERE username = :user");
    query.bindValue(":user", username.trimmed());
    if(!query.exec())
        qFatal("Unable to execute query.");

    query.next();
    this->_agentId = query.value(0).toInt();
}
int DatabaseController::getAgent()
{
    return this->_agentId;
}

void DatabaseController::saveAgent(Agent save){
    QSqlQuery query(this->_db);

    QString idString = QString::number(save.getId());

    query.prepare("UPDATE users SET first_name=:fname, last_name=:lname , username=:uname , email=:email , password=:pass WHERE id=:idString");
    query.bindValue(":fname", save.getFirstName());
    query.bindValue(":lname", save.getLastName());
    query.bindValue(":uname", save.getUsername());
    query.bindValue(":email", save.getEmail());
    query.bindValue(":pass", save.getPassword());
    query.bindValue(":idString", idString);
    if(!query.exec()){
        qFatal("Unable to execute query. saveAgent");
    }


}

bool DatabaseController::emailExists(QString email){
    QSqlQuery query(this->_db);

    query.prepare("SELECT * FROM users WHERE email= :email");
    query.bindValue(":email", email);

    if(!query.exec()){
        qFatal("Unable to execute query in emailExists");
    }

    if(query.next())
        return true;
    return false;
}

bool DatabaseController::userExists(QString username){
    QSqlQuery query(this->_db);

    query.prepare("SELECT * FROM users WHERE username=:uname");
    query.bindValue(":uname", username);

    if(!query.exec()){
        qFatal("Unable to execute query in userExists");
    }

    if(query.next())
        return true;
    return false;
}

std::vector<Comment> DatabaseController::getComments(int ticketID){

    std::vector<Comment> comments;
    QSqlQuery query(this->_db);
    QString idString = QString::number(ticketID);


    query.prepare("SELECT * FROM comments WHERE ticket_id=:idString");
    query.bindValue(":idString", idString);

    if(!query.exec()){
        qFatal("Unable to execute query in getComments");
    }

    while(query.next())
    {
        Comment temp(query.value(0).toInt(),query.value(1).toInt(),query.value(2).toInt(),query.value(3).toString(),query.value(4).toDateTime());     /////////////////////////////////
        comments.push_back(temp);
    }
    return comments;
}

Agent DatabaseController::getAgent(int id){
    QSqlQuery query(this->_db);

    QString idString = QString::number(id);
    query.prepare("SELECT id, first_name, last_name, email, password, role, username FROM users WHERE id= :idString");
    query.bindValue(":idString", idString);
    if(!query.exec()){
        qFatal("Unable to execute query. getAgent(int)");
    }
    query.first();

    Agent current(query.value(0).toInt(),query.value(1).toString(),query.value(2).toString(),query.value(3).toString(),query.value(4).toString(),query.value(5).toString(),query.value(6).toString());
    return current;
}

QSqlQuery DatabaseController::getSeverities(){

    QSqlQuery query(this->_db);

    query.prepare("SELECT * from severities LIMIT 10");

    if (!query.exec()){
        qFatal("Unable to execute query.");
    }

    return query;
}

QSqlQuery DatabaseController::getCategories() {

    QSqlQuery query(this->_db);

    query.prepare("SELECT * from categories LIMIT 10");

    if (!query.exec()){
        qFatal("Unable to execute query.");
    }

    return query;
}


void DatabaseController::saveCustomer(Customer* c){
    QSqlQuery query(this->_db);

    QString idString = QString::number(c->getId());

    query.prepare("UPDATE users SET first_name= :fname, last_name=:lname, phone=:phone, email=:email, password=:pass WHERE id=:idString");
    query.bindValue(":fname", c->getFirstName());
    query.bindValue(":lname", c->getLastName());
    query.bindValue(":phone", c->getPhone());
    query.bindValue(":email", c->getEmail());
    query.bindValue(":pass", c->getPassword());
    query.bindValue(":idString", idString);

    if(!query.exec()){
        qFatal("Unable to execute query. saveCustomer");
    }


}

bool DatabaseController::is_connected()
{
    return this->_connectedFlag;
}

QString DatabaseController::getFullNameByID(int id){
    QSqlQuery query(this->_controller->_db);

    //select
    QString stringID = QString::number(id);
    query.prepare("SELECT first_name, last_name FROM users WHERE id =:stringID");
    query.bindValue(":stringID", stringID);

    if (!query.exec())
    {
        qFatal("Unable to execute query.");
    }

    query.first();
    return query.value(0).toString() +" "+ query.value(1).toString();
}

void DatabaseController::insertComment(int ticketID, int agentID, QString body, QDateTime time){
    QSqlQuery query(this->_controller->_db);

    QString ticket = QString::number(ticketID);
    QString agent = QString::number(agentID);

    query.prepare("INSERT INTO comments (user_id,ticket_id,body,created_at) VALUES(:agent,:ticket, :body, :time)");
    query.bindValue(":agent", agent);
    query.bindValue(":ticket", ticket);
    query.bindValue(":body", body);
    query.bindValue(":time", time.toString("yyyy-MM-dd hh:mm:ss"));

    if (!query.exec())
    {
        qFatal("Unable to execute query. insertComment");
    }
}

void DatabaseController::deleteComment(int commentID){
    QSqlQuery query(this->_controller->_db);

    QString id = QString::number(commentID);

    query.prepare("DELETE FROM comments WHERE id=:id");
    query.bindValue(":id", id);

    if (!query.exec())
    {
        qFatal("Unable to execute query. insertComment");
    }
}

void DatabaseController::editComment(int commentID,QString body){
    QSqlQuery query(this->_db);

    QString idString = QString::number(commentID);

    query.prepare("UPDATE comments SET body=:body WHERE id=:idString");
    query.bindValue(":body", body);
    query.bindValue(":idString", idString);

    if(!query.exec()){
        qFatal("Unable to execute query. saveCustomer");
    }
}

  QSqlQuery DatabaseController::search(QString firstname,QString lastname,int offset,QString subscription,int index){
      QSqlQuery query(this->_db);
      firstname.replace("'","\\'");
      lastname.replace("'","\\'");
      if (lastname.compare("NOT_PROVIDED")==0)
          lastname="";
      if(subscription.compare("None")==0)
          subscription="";

      QString sql = "SELECT u.first_name as 'First Name', u.last_name as 'Last Name', u.email as Email, u.phone as Phone, p.name as Plan, u.ID FROM users u INNER JOIN plans p ON u.plan_id = p.id WHERE role='customer' AND first_name LIKE '"+firstname+"%'AND last_name LIKE '"+lastname+"%' AND p.name LIKE '%" + subscription+"%' ORDER BY u.first_name";

      this->_lastCustomerListQuery = sql;
      sql+= " LIMIT "+QString::number(offset)+" OFFSET "+QString::number(index);

      query.prepare(sql);


      if(!query.exec()){
          qFatal("Unable to execute query. search");
      }
      //if first name fails, search by last name instead
      if(query.size()==0 && firstname.compare("")!=0)
          return this->search("",firstname,offset,subscription,index);


      return query;
  }

void DatabaseController::insertNote(int ticketID, int agentID, QString body, QDateTime time){
    QSqlQuery query(this->_controller->_db);
    QString ticket = QString::number(ticketID);
    QString agent = QString::number(agentID);
    QString date = time.toString("yyyy-MM-dd hh:mm:ss");

    query.prepare("INSERT INTO internal_notes (user_id, body, created_at, ticket_id) VALUES(:agent, :body, :date, :ticket)");
    query.bindValue(":agent", agent);
    query.bindValue(":body", body);
    query.bindValue(":date", date);
    query.bindValue(":ticket", ticket);

    if (!query.exec())
    {
        qFatal("Unable to execute query. insert note");
    }
}

void DatabaseController::deleteNote(int noteID){
    QSqlQuery query(this->_controller->_db);

    QString id = QString::number(noteID);

    query.prepare("DELETE FROM internal_notes WHERE id=:id");
    query.bindValue(":id", id);

    if (!query.exec())
    {
        qFatal("Unable to execute query. deletenote");
    }
}

void DatabaseController::editNote(int noteID, QString body){
    QSqlQuery query(this->_db);
    QString idString = QString::number(noteID);

    query.prepare("UPDATE internal_notes SET body=:body WHERE id=:idString");
    query.bindValue(":body", body);
    query.bindValue(":idString", idString);

    if(!query.exec()){
        qFatal("Unable to execute query. savenote");
    }
}

std::vector<Comment> DatabaseController::getNotes(int ticketID){

    std::vector<Comment> notes;
    QSqlQuery query(this->_db);
    QString idString = QString::number(ticketID);


    query.prepare("SELECT * FROM internal_notes WHERE ticket_id=:idString");
    query.bindValue(":idString", idString);

    if(!query.exec()){
        qFatal("Unable to execute query in get notes");
    }

    while(query.next())
    {
        Comment temp(query.value(0).toInt(),query.value(1).toInt(),query.value(4).toInt(),query.value(2).toString(),query.value(3).toDateTime());     /////////////////////////////////
        notes.push_back(temp);
    }
    return notes;
}

int DatabaseController::getAgentOfTicket(int ticketID){
    QSqlQuery query(this->_db);

    QString idString = QString::number(ticketID);

    query.prepare("select * from tickets where id=:idString");
    query.bindValue(":idString", idString);

    if(!query.exec()){
        qFatal("Unable to execute query.");
    }
    int agent = 0;
    while (query.next()){
        agent = query.value(7).toInt();
    }
    return agent;
}
vector<QString> DatabaseController::getChanges()
 {

     return this->_changes;

 }

void DatabaseController::storeChanges(vector<QString> changes){

    this->_changes = changes;

}


