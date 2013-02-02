#ifndef PLAN_H
#define PLAN_H

/**
 * \class Plan
 * \author group 2
 *
 * Computer Science 3307a
 * Final Project
 *
 * \brief class that represents a subscription plan of a customer
 * of the Frogers company.
 */

#include <string>
using std::string;

class Plan
{
public:
    /**
     * \brief Plan constructor
     * \param id unique id of plan
     * \param price price of the plan
     * \param hours time of allowed connection
     * \param bandwidth monthly bandwidth cap
     * \param space virtual space supplied
     * \param name name of the plan
     */
    Plan(int id, int price, int hours, int bandwidth, int space, string name);

private:
    /**
     * \brief _id stored id of the plan
     */
    int _id;

    /**
     * \brief _price stored price of the plan
     */
    int _price;

    /**
     * \brief _hours stored monthly hourly cap of plan
     */
    int _hours;

    /**
     * \brief _bandwidth stored monthly bandwidth cap of plan
     */
    int _bandwidth;

    /**
     * \brief _space stored virtual space supplied of plan
     */
    int _space;

    /**
     * \brief _name stored name of plan
     */
    string _name;
};

#endif // PLAN_H
