/*
 * Model.cpp
 *
 *  Created on: Jun 27, 2013
 *      Author: birgmei
 */

#include <iostream>
#include <fstream>
#include <vector>
#include <stdexcept>
#include <assert.h>
#include <mathsat.h>
#include <sstream>

#include "Model.h"

using namespace std;

Model::Model(string progPrefix, vector<string> & abstrs) :
		abstrs(abstrs) {
	for (char x = 'a'; x <= 'z'; ++x) {
		varsToPrime.insert(
				VarPrimeMap::value_type(string(1, x), string(1, x) + "_p"));
	}
	varsToPrime.insert(VarPrimeMap::value_type(".s", ".s_p"));
	for (char x = 'a'; x <= 'z'; ++x) {
		primeToVars.insert(
				VarPrimeMap::value_type(string(1, x) + "_p", string(1, x)));
	}
	primeToVars.insert(VarPrimeMap::value_type(".s_p", ".s"));

	ifstream trFile(progPrefix + ".tr");
	std::string t;
	while (getline(trFile, t)) {
		tr.push_back(t);
	}
	trFile.close();

	if (abstrs.size() == 0) {
		ifstream abstrFile(progPrefix + ".abstr");
		while (getline(abstrFile, t)) {
			this->abstrs.push_back(t);
		}
		abstrFile.close();
	}

	ifstream asrtsFile(progPrefix + ".asrts");
	while (getline(asrtsFile, t)) {
		this->asrts.push_back(t);
	}
	asrtsFile.close();

	ifstream initFile(progPrefix + ".init");
	while (getline(initFile, t)) {
		this->inits.push_back(t);
	}
	initFile.close();
}

Model::~Model() {
}

void Model::loadTransitionRelation(msat_env env) const {
	assert(tr.size() > 0);
	for (vector<string>::const_iterator it = tr.begin(); it != tr.end(); ++it) {
		msat_term formula = msat_from_string(env, it->c_str());
		if (MSAT_ERROR_TERM(formula)) {
			cerr << "Could not parse formula: " << *it << endl;
			exit(3);
		}
		msat_assert_formula(env, formula);
	}
}

void Model::loadInitialCondition(msat_env env) const {
	for (vector<string>::const_iterator it = inits.begin(); it != inits.end();
			++it) {
		msat_term initial = msat_from_string(env, it->c_str());
		assert(!MSAT_ERROR_TERM(initial));
		msat_assert_formula(env, initial);
	}
}

void Model::loadError(msat_env env) const {
	for (vector<string>::const_iterator it = asrts.begin(); it != asrts.end();
			++it) {
		msat_term formula = msat_from_string(env, it->c_str());
		assert(!MSAT_ERROR_TERM(formula));
		msat_assert_formula(env, formula);
	}
}

msat_env Model::newSolver(void) const {
	msat_config cfg;
	msat_env env;
	cfg = msat_create_config();
	env = msat_create_env(cfg);
	msat_destroy_config(cfg);
	msat_type ift = msat_get_integer_type(env);
	for (VarPrimeMap::const_iterator it = varsToPrime.begin();
			it != varsToPrime.end(); ++it) {
		msat_decl d = msat_declare_function(env, it->first.c_str(), ift);
		assert(!MSAT_ERROR_DECL(d));
		msat_decl primedD = msat_declare_function(env, it->second.c_str(), ift);
		assert(!MSAT_ERROR_DECL(primedD));
	}
	return env;
}

msat_term Model::primeTerm(msat_env env, msat_term term) const {
	msat_type ift = msat_get_integer_type(env);
	if (msat_type_equals(msat_term_get_type(term), ift)) {
		char * varName = msat_decl_get_name(msat_term_get_decl(term));
		assert(varName);
		try {
			string primedVarName = varsToPrime.at(varName);
			msat_decl primedD = msat_declare_function(env,
					primedVarName.c_str(), ift);
			assert(!MSAT_ERROR_DECL(primedD));
			msat_term primedVar = msat_make_term(env, primedD, NULL);
			msat_free(varName);
			return primedVar;
		} catch (out_of_range &) {
		}
		msat_free(varName);
	}
	msat_term args[msat_term_arity(term)];
	for (size_t i = 0; i < msat_term_arity(term); ++i) {
		args[i] = primeTerm(env, msat_term_get_arg(term, i));
	}
	return msat_make_term(env, msat_term_get_decl(term), args);
}

msat_term Model::unprimeTerm(msat_env env, msat_term term) const {
	msat_type ift = msat_get_integer_type(env);
	if (msat_type_equals(msat_term_get_type(term), ift)) {
		char * varName = msat_decl_get_name(msat_term_get_decl(term));
		assert(varName);
		try {
			string unprimedVarName = primeToVars.at(varName);
			msat_decl unprimedD = msat_declare_function(env,
					unprimedVarName.c_str(), ift);
			assert(!MSAT_ERROR_DECL(unprimedD));
			msat_term unprimedVar = msat_make_term(env, unprimedD, NULL);
			msat_free(varName);
			return unprimedVar;
		} catch (out_of_range &) {
		}
		msat_free(varName);
	}
	msat_term args[msat_term_arity(term)];
	for (size_t i = 0; i < msat_term_arity(term); ++i) {
		args[i] = unprimeTerm(env, msat_term_get_arg(term, i));
	}
	return msat_make_term(env, msat_term_get_decl(term), args);
}

void Model::loadAbstrs(vector<msat_term> & terms, msat_env env) const {
	for (vector<string>::const_iterator it = abstrs.begin(); it != abstrs.end();
			++it) {
		msat_term term = msat_from_string(env, it->c_str());
		if (MSAT_ERROR_TERM(term)) {
			cerr << "Cannot parse MSAT term " << *it << endl;
			exit(5);
		}
		msat_term unprimed = unprimeTerm(env, term);
		terms.push_back(unprimed);
	}
}

string & Model::abstr(size_t idx) {
	return abstrs[idx];
}

string Model::stateToString(const AbstractStatePC & state) const {
	stringstream out;
	out << "PC " << state.first << " -- ";
	for (AbstractState::const_iterator it = state.second.begin();
			it != state.second.end(); ++it) {
		assert(*it != 0);
		if (*it > 0) {
			out << abstrs[*it - 1] << " ";
		} else {
			out << "(not " << abstrs[abs(*it) - 1] << ") ";
		}
	}
	return out.str();
}
