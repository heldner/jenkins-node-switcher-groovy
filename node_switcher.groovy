/*** BEGIN META {
"name" : "Mark all agents temporary offline",
"comment" : "Mark all agents temporary offline",
"parameters" : [
  { "enable - may be true or false",
    "regex - any regular expression"
  }
],
"core": "1.1",
"authors" : [
  { name : "Stanislav Korolev - skorolev@tradingview.com" }
]
} END META ***/

import hudson.model.Node
import hudson.model.Slave
import jenkins.model.Jenkins


def BusyNodeList = []
def jenkinsNodes = Jenkins.instance.nodes

def nodeEnabler(node, newstate, expression=''){
  // Make sure slave is offline and that the slave is free of running jobs
  String node_name = node.nodeName;
  if (expression != '') {
    if (node_name.matches(expression)) {
      println("nodename ${node_name} matches expression = ${expression}")
      nodeSwitcher(node, newstate)
    }
  } else {
    nodeSwitcher(node, newstate)
  }
}

def nodeSwitcher(node, nstate) {
  switch (nstate) {
    case 'true':
      state = 'enable'
      tempOffline = false
      break;
    case 'false':
      state = 'disable'
      tempOffline = true
      break;
    default:
      println("unknown state")
      return false;
  }
  //println("try to swtch node ${node} to state ${state}")
  switchNode(node, state, tempOffline)
}

def switchNode(node, state, tempOffline) {
  nodeOffline = node.getComputer().isOffline()
  if (nodeOffline && state == 'enable') // && node.getComputer().countBusy()==0)
  {
    println "${state} ${node.nodeName}"
    node.getComputer().setTemporarilyOffline(tempOffline, null);
  }
  if (!nodeOffline && state == 'disable')
  {
    println "${state} ${node.nodeName}"
    node.getComputer().setTemporarilyOffline(tempOffline, null);
  }
}

for (Node node in jenkinsNodes)
{
  nodeEnabler(node, enable, regex)
}
