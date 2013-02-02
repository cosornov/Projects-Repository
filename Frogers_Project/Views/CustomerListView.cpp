#include "Views/CustomerListView.h"
#include "ui_CustomerListView.h"
#include "Views/EditCustomerView.h"
#include "QErrorMessage"
#include "QSqlQueryModel"
#include "Controllers/CustomerListController.h"
#include <QShortcut>
#include <iostream>
#include <QDebug>
#include <QSortFilterProxyModel>
#include <QTreeView>
#include <QItemSelectionModel>
#include <QModelIndexList>
#include <QAbstractItemModel>
#include <QFile>
#include <QCompleter>
#include <phonon/AudioOutput>
#include <phonon/MediaObject>


CustomerListView::CustomerListView(QMainWindow *parent) :
    QMainWindow(parent),
    ui(new Ui::CustomerListView)
{

    ui->setupUi(this);

    //
    // CUSTOM CSS AREA
    //

    // Custom Green Button
    QFile data(":/new/prefix1/greenButton.qss");
    data.open(QFile::ReadOnly);
    QTextStream styleIn(&data);
    this->_greenButton = styleIn.readAll();
    data.close();

    // Create a box to allow selection of various pre-determined amounts of customers
    ui->customerInput->addItem("10");
    ui->customerInput->addItem("25");
    ui->customerInput->addItem("50");
    ui->customerInput->addItem("100");

    // The default amount is 25 customers
    ui->customerInput->setCurrentIndex(1);

    // Create a box to allow selection of various pre-determined subscription filters
    ui->subscriptionInput->addItem("None");
    ui->subscriptionInput->addItem("Lite");
    ui->subscriptionInput->addItem("Casual Surfer");
    ui->subscriptionInput->addItem("Internet Addict");
    ui->subscriptionInput->addItem("Unlimited");

    this->_controller = CustomerListController();

    // By default show the first 25 customers from the database
    this->_currentIndex = 0;
    this->_offset = 25;

    // Convert the indexes to QStrings to make querying easier
    QString currentIndex = QString::number(this->_currentIndex);
    QString offset =  QString::number(this->_offset);

    // Load the data
    _model1 = this->_controller.loadCustomers(currentIndex, offset, ui->subscriptionInput->currentText());
    _model2 = new QSortFilterProxyModel();

    _model2->setSourceModel(_model1);

    // Insert the customers into the table
    this->ui->tableView->setModel(_model2);

    // Show the customers from 1..X, X being the number of customers selected
    this->ui->tableView->sortByColumn(0, Qt::AscendingOrder);
    this->ui->tableView->setSortingEnabled(true);
    this->ui->tableView->setColumnHidden(5,true);

    // Custom refresh icon
    QPixmap pixmap(":/new/prefix1/refresh2.png");
    QIcon ButtonIcon(pixmap);
    ui->refreshButton->setIcon(ButtonIcon);
    ui->refreshButton->setIconSize(pixmap.rect().size());

    // Set the buttons to their pre-determined roles (enabled or disabled)
    updateButtonData();

    //
    // SIGNALS AND SLOTS
    //

    // Connect all the page buttons to their respective functions
    connect(ui->nextPageButton,SIGNAL(clicked()), this, SLOT(nextPageButtonClicked()));
    connect(ui->previousPageButton,SIGNAL(clicked()), this, SLOT(previousPageButtonClicked()));
    connect(ui->lastPageButton,SIGNAL(clicked()), this, SLOT(lastPageButtonClicked()));
    connect(ui->firstPageButton,SIGNAL(clicked()), this, SLOT(firstPageButtonClicked()));

    // The search bar should run a search on each user input
    connect(ui->searchBar,SIGNAL(textChanged(QString)),this,SLOT(search()));

    // Load fresh data upon click
    connect(ui->refreshButton,SIGNAL(clicked()),this,SLOT(updateData()));

    // Identify when the user has selected a new number of customers to display
    connect(ui->customerInput,SIGNAL(currentIndexChanged(int)), this, SLOT(updateData()));

    // Allow double-clicking of the customers to open up THE EDIT CUSTOMER window
    connect(ui->tableView, SIGNAL(doubleClicked(QModelIndex)), this, SLOT(editCustomer()));

    // Change the filter if the user selected a different one
    connect(ui->subscriptionInput, SIGNAL(currentIndexChanged(int)),
                this, SLOT(updateData()));
}

CustomerListView::~CustomerListView()
{
    delete ui;
}

void CustomerListView::editCustomer(){
        // Create a new Edit Customer view to show
        EditCustomerView *view = new EditCustomerView()
                ;
        QItemSelectionModel *selectModel = ui->tableView->selectionModel();
        if (selectModel->hasSelection()){

            // Index the correct customer
            QModelIndexList list = selectModel->selectedRows();
            QModelIndex row = selectModel->currentIndex();
            QString stringID = ui->tableView->model()->data(ui->tableView->model()->index(row.row(), 5)).toString();

            // Fill the Edit Customer form with the currently selected customers information
            view->getController()->InjectCustomer(stringID.toInt());

            view->fillForm();
            view->exec();
        }
}

void CustomerListView::setButtonModel(){

    QString currentIndex2 = QString::number(this->_currentIndex);
    QString offset2 =  QString::number(this->_offset);

    // If there is nothing in the text box aka they backspaced to nothing just reload the non-filtered data
    if(ui->searchBar->text().size()==0){
        _model1 = this->_controller.loadCustomers(currentIndex2, offset2, ui->subscriptionInput->currentText());
        // Put the customers in the table
        this->ui->tableView->setModel(_model1);
    }
    else
        this->search();



    updateButtonData();
}

void CustomerListView::updateData(){
    this->ui->tableView->sortByColumn(0, Qt::AscendingOrder);
    this->ui->tableView->setSortingEnabled(true);

    this->_currentIndex = 0;

    // Set the new offest by the QComboBox selection
    this->_offset = ui->customerInput->currentText().toInt();

    QString currentIndex = QString::number(this->_currentIndex);
    QString offset =  QString::number(this->_offset);
    QSqlQueryModel* model;

    // Re-load new data
    if(ui->searchBar->text().size()==0){
        model = this->_controller.loadCustomers(currentIndex, offset, ui->subscriptionInput->currentText());
        this->_model2->setSourceModel(model);
    }
    else
        this->search();

}

void CustomerListView::updateButtonData(){

    this->ui->tableView->sortByColumn(0, Qt::AscendingOrder);
    this->ui->tableView->setSortingEnabled(true);

    // Check whether the previous and next buttons can't go back any more
    if ((this->_currentIndex - this->_offset) < 0){
        // Disable the previous button
        ui->previousPageButton->setDisabled(true);
        ui->previousPageButton->setStyleSheet("QPushButton { background: grey; }");
        ui->firstPageButton->setDisabled(true);
        ui->firstPageButton->setStyleSheet("QPushButton { background: grey; }");
    } else {
        // Enable the previous button / first page button as we're not looking at the first page
        ui->previousPageButton->setDisabled(false);
        ui->previousPageButton->setStyleSheet(this->_greenButton);
        ui->firstPageButton->setDisabled(false);
        ui->firstPageButton->setStyleSheet(this->_greenButton);
    }

    // Check whether the current index would be greater than the total of number of entries available
    if ((this->_currentIndex + this->_offset) >= this->_controller.resultCount()) {
            // Disable the next button / last page button as we're looking at the first 25 entries
            ui->nextPageButton->setDisabled(true);
            ui->nextPageButton->setStyleSheet("QPushButton { background: grey; }");
            ui->lastPageButton->setDisabled(true);
            ui->lastPageButton->setStyleSheet("QPushButton { background: grey; }");
    } else {
            // There are stlil some pages to see
            ui->nextPageButton->setDisabled(false);
            ui->nextPageButton->setStyleSheet(this->_greenButton);
            ui->lastPageButton->setDisabled(false);
            ui->lastPageButton->setStyleSheet(this->_greenButton);
    }
}

void CustomerListView::nextPageButtonClicked(){
    this->_currentIndex = this->_currentIndex  + this->_offset;

    setButtonModel();
}

void CustomerListView::previousPageButtonClicked(){

   this->_currentIndex = this->_currentIndex - this->_offset;

   if (this->_currentIndex < 0) {
       this->_currentIndex = 0;
   }

    setButtonModel();
}

void CustomerListView::lastPageButtonClicked(){

    this->_currentIndex = this->_controller.resultCount() - this->_offset;

    setButtonModel();
}

 void CustomerListView::firstPageButtonClicked(){

    this->_currentIndex = 0;

     setButtonModel();
}

void CustomerListView::search(){

    QString search = ui->searchBar->text();

    if(this->_controller.isInappropriate(search)){        
          // Easter egg. search for curse words :)
          Phonon::MediaObject *file = new Phonon::MediaObject(this);
          Phonon::AudioOutput *audio = new Phonon::AudioOutput(Phonon::MusicCategory, this);
          Phonon::createPath(file, audio);
          file->setCurrentSource(Phonon::MediaSource(":/new/prefix1/cage.m4a"));
          file->play();
    }

    if(search.size()!=0){
        QSqlQueryModel* model;
        int offset = this->_offset;
        int index = this->_currentIndex;
        int containsSpace = search.indexOf(" ");
        QString lastname;
        QString firstname=search;
        if(containsSpace==-1)
            lastname = "NOT_PROVIDED";
        else{
            firstname = search.left(containsSpace);
            lastname = search.section(" ",1);
        }
        model =this->_controller.search(offset,firstname,lastname,ui->subscriptionInput->currentText(),index);
        this->_model2->setSourceModel(model);

        this->_model2->setSourceModel(model);


        updateButtonData();

        // Set up auto completer
        QCompleter *completer = new QCompleter(this->_controller.getWordList(), this);
        completer->setCaseSensitivity(Qt::CaseInsensitive);
        ui->searchBar->setCompleter(completer);
    }
    else
        this->updateData();
}
