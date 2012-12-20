import string
import requests
import random
from collections import defaultdict
from multiprocessing import Process, Queue


NUM_PROCS = 10
NUM_REQUESTS = 10000
VOTE_VALUES = 12
SERVER = 'http://localhost:8080/'
ADD_URI = '%s%s' %(SERVER,'member')
VOTE_URI = '%s%s' %(SERVER,'vote')
VICTORY_URI = '%s%s' %(SERVER,'victory')
RST_URI = '%s%s' %(SERVER,'rst')


def randString(n=10):
  return ''.join(random.choice(string.ascii_uppercase + string.digits) for x in range(n))

def addAgent(member):
  parm = {'agent':member}
  return requests.post(ADD_URI,parm).status_code

def addVote(member,vote):
  parm = {'agent':member,'vote':vote}
  return requests.post(VOTE_URI,parm).status_code

def getVictory():
  return requests.get(VICTORY_URI).status_code

def reset():
  return requests.post(RST_URI).status_code

def issue_requests(vote_values, options, q):
  results = defaultdict(lambda: defaultdict(int))

  for i in range(NUM_REQUESTS):
    opt = random.choice(options)
    if opt == 'ADD':
      results[opt][addAgent(randString())]+=1
    elif opt == 'ADD_VOTE':
      name = randString()
      results['ADD'][addAgent(name)]+=1
      results[opt][addVote(name, random.choice(vote_values))]+=1
    elif opt == 'VICTORY':   
      results[opt][getVictory()]+=1

  str_res = []
  str_res.append("%-10s%-10s%s" %('Type','St. Code','Count'))
  for key,value in results.items():
    str_res.append(key)
    for k2,v2 in results[key].items():
      str_res.append("%-10s%-10s%s" %('',k2,v2))
  print '\n'.join(str_res)
  q.put(str_res)
    
if __name__ == "__main__":
  print "Reseting:" , reset()
  vote_values = []
  for i in range(VOTE_VALUES):
    vote_values.append(randString())
        
  options = ['ADD_VOTE']*6
  options.extend(['ADD']*3)
  options.append('VICTORY')

  q = Queue()
  procs = []
  for p in range(NUM_PROCS):
    procs.append(Process(target = issue_requests, args=(vote_values,options,q)))
  for p in procs:
    p.start()
  for p in procs:
    p.join()

