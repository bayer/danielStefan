/*
 * Model.h
 *
 *  Created on: Jun 27, 2013
 *      Author: birgmei
 */

#ifndef MODEL_H_
#define MODEL_H_

#include <map>
#include <vector>

#include <mathsat.h>

using namespace std;

typedef map<string, string> VarPrimeMap;
typedef vector<long> AbstractState;
typedef pair<unsigned, AbstractState> AbstractStatePC;

class Model {

	vector<string> tr;
	VarPrimeMap varsToPrime;
	VarPrimeMap primeToVars;

public:

	vector<string> & abstrs;
	vector<string> asrts;
	vector<string> inits;

	Model(string progPrefix, vector<string> & abstrs);

	virtual ~Model();

	void loadTransitionRelation(msat_env env) const;

	void loadInitialCondition(msat_env env) const;

	void loadError(msat_env env) const;

	msat_term primeTerm(msat_env env, msat_term term) const;

	msat_term unprimeTerm(msat_env env, msat_term term) const;

	msat_env newSolver(void) const;

	void loadAbstrs(vector<msat_term> & abstrVec, msat_env env) const;

	string & abstr(size_t idx);

	string stateToString(const AbstractStatePC & state) const;

};

#endif /* MODEL_H_ */
