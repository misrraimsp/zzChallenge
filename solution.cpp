#include<iostream>
#include<set>
#include<vector>

using namespace std;

struct Element {
long long height;
long long acc;
long long con;
};

bool fncomp(Element* lhs, Element* rhs) {
    return lhs->height < rhs->height;
}

int solution(vector<int> &H) {
    // set up
    int N = (int)H.size();
    if (N == 0 || N == 1) return N;
    long long sol = 0;
    // build trees
    bool(*fn_pt)(Element*, Element*) = fncomp;
    set<Element*, bool(*)(Element*, Element*)> rightTree(fn_pt),   leftTree(fn_pt);
    set<Element*, bool(*)(Element*, Element*)>::iterator ri, li;
    for (int i = 0; i < N; i++) {
        Element* e = new Element;
        e->acc = 0;
        e->con = 0;
        e->height = H[i];
        rightTree.insert(e);
    }
    //tree elements set up
    ri = --rightTree.end();
    Element* elem = *ri;
    elem->con = 1;
    elem->acc = 1;
    while (elem->height > H[0]) {
        Element* succ = elem;
        ri--;
        elem = *ri;
        elem->con = 1;
        elem->acc = succ->acc + 1;
    }
    rightTree.erase(ri);
    elem->con = elem->acc;
    leftTree.insert(elem);
    sol += elem->acc;
    // main loop
    Element* pE = new Element;
    for (int j = 1; j < (N - 1); j++) {
        // bad case
        if (H[j] < H[j - 1]) {
            ///////
            Element* nE = new Element;
            nE->height = H[j];
            pE->height = H[j - 1];
            rightTree.erase(nE);
            leftTree.insert(nE);
            ///////
            li = leftTree.lower_bound(pE);
            long ltAcc = (*li)->acc;
            li--;
            ///////
            ri = rightTree.lower_bound(pE);
            long rtAcc = 0;
            if (ri != rightTree.end()) rtAcc = (*ri)->acc;
            ri--;
            ///////
            while (ri != (--rightTree.begin()) && (*ri)->height > H[j]) {
                if (fncomp(*ri, *li)) {
                    (*li)->con = rtAcc + 1;
                    (*li)->acc = rtAcc + 1 + ltAcc;
                    ltAcc = (*li)->acc;
                    --li;
                }
                else {
                    (*ri)->con = ltAcc + 1;
                    (*ri)->acc = ltAcc + 1 + rtAcc;
                    rtAcc = (*ri)->acc;
                    --ri;
                }
            }
            while ((*li)->height > H[j]) {
                (*li)->con = rtAcc + 1;
                (*li)->acc = rtAcc + 1 + ltAcc;
                ltAcc = (*li)->acc;
                --li;
            }
            (*li)->con = rtAcc + 1;
            (*li)->acc = rtAcc + 1 + ltAcc;
            sol += (*li)->acc;
        }
        // good case
        else {
            Element* nE = new Element;
            nE->height = H[j];
            ri = rightTree.upper_bound(nE);
            li = leftTree.upper_bound(nE);
            rightTree.erase(nE);
            if (li == leftTree.end() && ri == rightTree.end()) {
                nE->con = 1;
                nE->acc = 1;
            }
            else if (li != leftTree.end() && ri == rightTree.end()) {
                nE->con = 1;
                nE->acc = 1 + (*li)->acc;
            }
            else if (li == leftTree.end() && ri != rightTree.end()) {
                nE->con = (*ri)->acc + 1;
                nE->acc = nE->con;
            }
            else {
                nE->con = (*ri)->acc + 1;
                nE->acc = nE->con + (*li)->acc;
            }
            leftTree.insert(nE);
            sol += nE->acc;
        }
    }
    // final step
    li = leftTree.upper_bound(*rightTree.begin());
    while (li != leftTree.end()) {
        sol++;
        li++;
    }
    sol++;
    return (int)(sol % 1000000007);
}

int main(int argc, char* argv[]) {
    vector<int> H = { 13, 2, 5 };
    cout << "sol: " << solution(H) << endl;
    system("pause");
}