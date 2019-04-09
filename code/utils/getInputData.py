import numpy as np

ROOT = "D:\\Code\\CommunityDetection\\data\\"
name = "karate"# "CA-HepTh"

def getInputData(name, mod=0):
    file = ROOT + "{}\\{}.txt".format(name, name)
    
    if mod == 0:
        outfile = ROOT + "{}\\{}_matlab.txt".format(name, name)
        getDataIndexFrom1(file, outfile)
    elif mod == 1:
        outfile = ROOT + "{}\\{}_changed_index.txt".format(name, name)
        getDataIndexChanged(file, outfile, mod)
    elif mod == 2:
        outfile = ROOT + "{}\\{}_matlab_changed_index.txt".format(name, name)
        getDataIndexChanged(file, outfile, mod)
    else:
        print("Error mod")
        
def getDataIndexFrom1(f1, f2):
    out = []
    with open(f1, "r") as f:
        for line in f :
            x, y = line.rstrip().split(" ")
            a = [int(x) + 1, int(y) + 1]
            out.append(a)
    out = np.array(out)
    np.savetxt(f2, out, fmt="%d")

def getDataIndexChanged(f1, f2, mod=1):
    out = []
    seen = {}
    if mod == 1:
        index = 0
    elif mod == 2:
        index = 1
    else:
        print("Not support mod!")
    
    with open(f1, "r") as f:
        for line in f:
            x, y = line.rstrip().split("\t")
            if x not in seen:
                seen[x] = index
                index += 1
            if y not in seen:
                seen[y] = index
                index += 1
            a = [seen[x], seen[y]]
            out.append(a)
    out = np.array(out)
    np.savetxt(f2, out, fmt="%d")

def checkDataUndirected(f1):
    seen = {}
    with open(f1, "r") as f:
        for line in f:
            x, y = line.rstrip().split(" ")
            w1 = " ".join([x, y])
            w2 = " ".join([y, x])
            if  w1 not in seen:
                seen[w1] = 0
            if w2 not in seen:
                seen[w2] = 0
            seen[w1] += 1
            seen[w2] += 1
    for k, v in seen.items():
        if v != 2:
            return False

    return True
if __name__ == "__main__":
    # getInputData(name, 2)
    print(checkDataUndirected(ROOT + "{}\\{}.txt".format(name, name)))