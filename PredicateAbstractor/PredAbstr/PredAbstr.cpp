/*
 * PredAbstr.cpp
 *
 *  Created on: Sep 4, 2013
 *      Author: birgmei
 */

#include <iostream>
#include <map>
#include <set>
#include <assert.h>
#include <mathsat.h>

#include "PredAbstr.h"

typedef vector<msat_term> TermVec;
typedef pair<AbstractStatePC, AbstractStatePC> Transition;
typedef vector<Transition> TransMap;
typedef set<AbstractStatePC> StateSet;

ostream & operator<<(ostream & stream, const AbstractState & asgn) {
	for (AbstractState::const_iterator it = asgn.begin(); it != asgn.end();
			++it) {
		stream << *it << " ";
	}
	return stream;
}

ostream & operator<<(ostream & stream, const AbstractStatePC & asgn) {
	stream << asgn.first << "/" << asgn.second;
	return stream;
}

class PredAbstr {

	Model & model;
	msat_env env;
	unsigned maxPc;
	unsigned long long numOps;
	unsigned long long ops;

	unsigned highestProgramCounter(void) {
		msat_push_backtrack_point(env);
		model.loadTransitionRelation(env);
		unsigned i = -1;
		bool cont = true;
		while (cont) {
			++i;
			msat_push_backtrack_point(env);
			string s = "(= .s " + to_string(i) + ")";
			msat_term test = msat_from_string(env, s.c_str());
			msat_assert_formula(env, test);
			msat_result rv = msat_solve(env);
			assert(rv == MSAT_SAT || rv == MSAT_UNSAT);
			if (rv == MSAT_UNSAT)
				cont = false;
			msat_pop_backtrack_point(env);
		}
		msat_pop_backtrack_point(env);
		assert(i >= 0);
		return i;
	}

	msat_term idxToTerm(TermVec & inputs, long idx) {
		if (idx < 0) {
			return msat_make_not(env, inputs[abs(idx) - 1]);
		} else {
			return inputs[idx - 1];
		}
	}

	void testCombinations(TransMap & out, AbstractState & state,
			TermVec & inputs, size_t inputIdx = 0) {
		if (inputIdx < inputs.size()) {
			state.push_back(inputIdx + 1);
			testCombinations(out, state, inputs, inputIdx + 1);
			state.pop_back();

			state.push_back(-(inputIdx + 1));
			testCombinations(out, state, inputs, inputIdx + 1);
			state.pop_back();
		} else {
			AbstractState innerState;
			testCombinationsInner(out, state, innerState, inputs);
		}
	}

	void testCombinationsInner(TransMap & out, AbstractState & outerState,
			AbstractState & state, TermVec & inputs, size_t inputIdx = 0) {
		if (inputIdx < inputs.size()) {
			state.push_back(inputIdx + 1);
			testCombinationsInner(out, outerState, state, inputs,
						inputIdx + 1);
			state.pop_back();

			msat_term negTerm = msat_make_not(env, inputs[inputIdx]);
			state.push_back(-(inputIdx + 1));
			testCombinationsInner(out, outerState, state, inputs,
						inputIdx + 1);
			state.pop_back();
		} else {
			testProgramCounterTransitions(out, outerState, state, inputs);
		}
	}

	void testProgramCounterTransitions(TransMap & out, AbstractState & state1,
			AbstractState & state2, TermVec & inputs) {
		for (unsigned i = 0; i <= maxPc; ++i) {
			for (unsigned j = 0; j <= maxPc; ++j) {
				msat_push_backtrack_point(env);
				for (AbstractState::const_iterator it = state1.begin();
						it != state1.end(); ++it) {
					msat_assert_formula(env, idxToTerm(inputs, *it));
				}
				for (AbstractState::const_iterator it = state2.begin();
						it != state2.end(); ++it) {
					msat_assert_formula(env,
							model.primeTerm(env, idxToTerm(inputs, *it)));
				}
				string pc1s = "(= .s " + to_string(i) + ")";
				string pc2s = "(= .s_p " + to_string(j) + ")";
				msat_term pc1 = msat_from_string(env, pc1s.c_str());
				msat_term pc2 = msat_from_string(env, pc2s.c_str());
				msat_assert_formula(env, pc1);
				msat_assert_formula(env, pc2);
				msat_result rv = msat_solve(env);
				assert(rv == MSAT_SAT || rv == MSAT_UNSAT);
				++ops;
				if (rv == MSAT_SAT) {
					out.push_back(
							Transition(AbstractStatePC(i, state1),
									AbstractStatePC(j, state2)));
				}
				msat_pop_backtrack_point(env);
			}
		}
		double rel = ((double) ops * 100) / numOps;
		cerr << rel << "%" << endl;
	}

	void testInit(StateSet & inits, const AbstractStatePC & state,
			TermVec & inputs) {
		msat_push_backtrack_point(env);
		string pcs = "(= .s " + to_string(state.first) + ")";
		msat_term pc = msat_from_string(env, pcs.c_str());
		msat_assert_formula(env, pc);
		for (AbstractState::const_iterator it = state.second.begin();
				it != state.second.end(); ++it) {
			msat_assert_formula(env, idxToTerm(inputs, *it));
		}
		msat_result rv = msat_solve(env);
		msat_pop_backtrack_point(env);
		assert(rv == MSAT_SAT || rv == MSAT_UNSAT);
		if (rv == MSAT_SAT) {
			inits.insert(state);
		}
	}

	void testInits(StateSet & inits, TransMap & trs, TermVec & inputs) {
		for (TransMap::const_iterator it = trs.begin(); it != trs.end(); ++it) {
			if (inits.find(it->first) == inits.end()) {
				testInit(inits, it->first, inputs);
			}
			if (inits.find(it->second) == inits.end()) {
				testInit(inits, it->second, inputs);
			}
		}
	}

	void testError(StateSet & errors, const AbstractStatePC & state,
			TermVec & inputs) {
		msat_push_backtrack_point(env);
		string pcs = "(= .s " + to_string(state.first) + ")";
		msat_term pc = msat_from_string(env, pcs.c_str());
		msat_assert_formula(env, pc);
		for (AbstractState::const_iterator it = state.second.begin();
				it != state.second.end(); ++it) {
			msat_assert_formula(env, idxToTerm(inputs, *it));
		}
		msat_result rv = msat_solve(env);
		msat_pop_backtrack_point(env);
		assert(rv == MSAT_SAT || rv == MSAT_UNSAT);
		if (rv == MSAT_UNSAT) {
			errors.insert(state);
		}
	}

	void testErrors(StateSet & errors, TransMap & trs, TermVec & inputs) {
		for (TransMap::const_iterator it = trs.begin(); it != trs.end(); ++it) {
			if (errors.find(it->first) == errors.end()) {
				testError(errors, it->first, inputs);
			}
			if (errors.find(it->second) == errors.end()) {
				testError(errors, it->second, inputs);
			}
		}
	}

	unsigned long long pow2(size_t size) {
		if (size == 0)
			return 1;
		return 2 * pow2(size - 1);
	}

public:

	PredAbstr(Model & model) :
			model(model), maxPc(0), numOps(0), ops(0) {
		env = model.newSolver();
	}

	~PredAbstr() {
		msat_destroy_env(env);
	}

	void transitions(TransMap & trs, StateSet & inits, StateSet & errors) {
		maxPc = highestProgramCounter();
		TermVec terms;
		model.loadAbstrs(terms, env);
		assert(terms.size() > 0);

		ops = 0;
		numOps = pow2(model.abstrs.size() * 2) * (maxPc + 1) * (maxPc + 1);
		msat_push_backtrack_point(env);
		TermVec combination;
		AbstractState state;
		model.loadTransitionRelation(env);
		testCombinations(trs, state, terms);
		msat_pop_backtrack_point(env);

		msat_push_backtrack_point(env);
		model.loadInitialCondition(env);
		testInits(inits, trs, terms);
		msat_pop_backtrack_point(env);

		msat_push_backtrack_point(env);
		model.loadError(env);
		testErrors(errors, trs, terms);
		msat_pop_backtrack_point(env);
	}

};

void check(Model & model, bool imageOut) {
	TransMap trs;
	StateSet inits;
	StateSet errors;
	PredAbstr abstractor(model);

	abstractor.transitions(trs, inits, errors);
	if (imageOut) {
		cout << "digraph G {" << endl;
		for (size_t i = 0; i < model.abstrs.size(); ++i) {
			cout << "\t\"" << (i + 1) << " ... " << model.abstrs[i]
					<< "\" [shape=box, color=lightblue, style=filled];" << endl;
			;
		}
		cout << "\t";
		for (size_t i = 0; i < model.abstrs.size(); ++i) {
			cout << "\"" << (i + 1) << " ... " << model.abstrs[i] << "\"";
			if (i < model.abstrs.size() - 1) {
				cout << " -> ";
			} else {
				cout << ";" << endl;
			}
		}
		for (StateSet::const_iterator it = inits.begin(); it != inits.end();
				++it) {
			cout << "\t\"" << *it << "\" [peripheries=2, color=green, style=filled];" << endl;
		}
		for (StateSet::const_iterator it = errors.begin(); it != errors.end();
				++it) {
			cout << "\t\"" << *it << "\" [color=red, style=filled];" << endl;
		}
		for (TransMap::const_iterator it = trs.begin(); it != trs.end(); ++it) {
			cout << "\t\"" << it->first << "\" -> \"" << it->second << "\";"
					<< endl;
		}
		cout << "}" << endl;
	} else {
		for (StateSet::const_iterator it = inits.begin(); it != inits.end();
				++it) {
			cout << "Init:" << endl << model.stateToString(*it) << endl << endl;
		}
		cout << endl;
		for (StateSet::const_iterator it = errors.begin(); it != errors.end();
				++it) {
			cout << "Init:" << endl << model.stateToString(*it) << endl << endl;
		}
		cout << endl;
		for (TransMap::const_iterator it = trs.begin(); it != trs.end(); ++it) {
			cout << model.stateToString(it->first) << endl << "->" << endl
					<< model.stateToString(it->second) << endl << endl;
		}
	}
}
