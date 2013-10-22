Dev Environments & Automation
==============================

Knight Capital
--------------
Up until December 2012, Knight Capital was the largest trader
in US equities, with a market share of roughly 17%. Knight Capital
used an automated trading platform to reach this market share. 

In December of 2012, a bug in Knight Capital's automated trading
platform led to the loss of $460 million in 45 minutes. The root
cause of the bug was actually a mistake caused by a manual deployment
process that included no automated testing of the deployed system:

"13. Upon deployment, the new RLP code in SMARS was intended to replace unused code in the relevant portion of the order router. This unused code previously had been used for functionality called “Power Peg,” which Knight had discontinued using many years earlier. Despite the lack of use, the Power Peg functionality remained present and callable at the time of the RLP deployment. The new RLP code also repurposed a flag that was formerly used to activate the Power Peg code. Knight intended to delete the Power Peg code so that when this flag was set to “yes,” the new RLP functionality—rather than Power Peg—would be engaged.

14. When Knight used the Power Peg code previously, as child orders were executed, a cumulative quantity function counted the number of shares of the parent order that had been executed. This feature instructed the code to stop routing child orders after the parent order had been filled completely. In 2003, Knight ceased using the Power Peg functionality. In 2005, Knight moved the tracking of cumulative shares function in the Power Peg code to an earlier point in the SMARS code sequence. Knight did not retest the Power Peg code after moving the cumulative quantity function to determine whether Power Peg would still function correctly if called.

15. Beginning on July 27, 2012, Knight deployed the new RLP code in SMARS in stages by placing it on a limited number of servers in SMARS on successive days. During the deployment of the new code, however, one of Knight’s technicians did not copy the new code to one of the eight SMARS computer servers. Knight did not have a second technician review this deployment and no one at Knight realized that the Power Peg code had not been removed from the eighth server, nor the new RLP code added. Knight had no written procedures that required such a review." 

Excerpted from: http://pythonsweetness.tumblr.com/post/64740079543/how-to-lose-172-222-a-second-for-45-minutes

Overview
----------
A fundamental concern is ensuring that developers expectations of how
their code will be deployed and operate in production matches reality.
Developers need to ensure that their development environments precisely
match production environments. Moreover, developers must ensure that their
code can be deployed accurately and repeatably into production. If errors
occur in the deployment process, they must be identified and rectified before
the code goes live. When errors do happen in production, it is critical
that developers have a unified window into the production systems to identify
the root cause of the failure.

This exercise will introduce you to three critical types of tools for managing
the transition and execution of code in production environments:

1. Mock Production Environments - A key to building code that functions correctly
in production is having a mock version of the production environment. Vagrant
is a tool, based on virtualization, that allows developers to build and share
mock production environments for development.

2. Environment Provisioning Automation - At cloud scale, with 10s-1000s of
servers, manually configuring servers with web servers, system tools, and
patches isn't scalable. Puppet is an IT automation tool that allows you to
specify an environment configuration (e.g., running Nginx x.y.z, build-essentials,
etc.) and consistently automate the provisioning of environments with that
configuration.

3. Production Monitoring Dashboards - With 10s-1000s of machines, centralized
monitoring and search of log files and system states is important to diagnose
unexpected errors (e.g., the stuff you didn't realize you should test for). 
Logstash & Kibana are a log monitoring and dashboard system that allow you to
track multiple distributed application logs across hosts and build a dashboard
to query, analyze, and visualize log events.

Prerequisites
-------------
1. Install Vagrant: http://docs.vagrantup.com/v2/installation/ 

2. (if needed) Install VirtualBox: https://www.virtualbox.org/wiki/Downloads

3. Clone the exercise repo

4. Cd into the exercise repo and type "vagrant up"


Helpful Guides
--------------

Vagrant: http://docs.vagrantup.com/v2/getting-started/

Puppet: http://docs.puppetlabs.com/learning/ral.html

Logstash: http://logstash.net/docs/1.2.1/tutorials/getting-started-simple

Helpful Notes
--------------

- A Vagrantfile has already been created for you by using the 
"vagrant init" command. This file has been edited to use Puppet
as the provisioning mechanism and open several ports.

- You can start your VM by typing "vagrant up" in the project directory
on the host (not the VM).

- You can ssh into your VM by typing "vagrant ssh" in the project directory
on the host (not the VM).

- You can destroy your VM completely (helpful to rerun the Puppet provisioning
from scratch) by running "vagrant destroy" in the project directory
on the host (not the VM).

- The root folder of the project will be shared with the VM and
visible at /vagrant

- Vagrant is setup in this exercise to automatically apply
the Puppet manifest in manifests/default.pp. You do not need to
worry about the client/server or puppet command line discussions. 
Simply "up," "reload," and "destroy" the VM to get it to apply
the Puppet manifest.

Exercise Instructions
----------------------

1. Update the Puppet script in mainfests/default.pp to install
the following packages: zip, curl, nginx, and openjdk-7-jre-headless

2. Ensure that zip, curl, and openjdk-7-jre-headless are
installed after apt-get update is run

3. Ensure that nginx is installed after zip

4. Update the Puppet script to start nginx as a service

5. Update the Puppet script to replace /etc/motd with the
contents of the motd file in the shared folder (mapped
to /vagrant in the VM)

6. Download the redis module (fsalum/redis) from Puppet Forge (https://forge.puppetlabs.com) and install it in the modules 
directory of the shared folder (hint: you should end up with the
folder "modules/redis" that includes the contents of "fsalum/redis"
from the tar archive). 

7. Update the Puppet script to install redis using the redis module

8. Update the Puppet script to automatically launch logstash using the 
following command:

nohup java -jar logstash/logstash-1.2.1-flatjar.jar agent -f logstash-simple.conf -- web > /dev/null 2>&1 &

You will probably want to set the following parameters as well:

  cwd => "/vagrant/",
  user => "vagrant",
  path => "/usr/bin/:/bin/",
  logoutput => true,

After launching the VM, if your configuration is correct, you should be able
to access the logstash dashboard here (it will take a LONG time to load and
you will have a blank white screen while it does):

(localhost will NOT work)
http://<your_ip_address>:9292/index.html#/dashboard

9. Edit logstash-simple.conf to add a file input to track the Nginx access
log in "/var/log/nginx". You will probably want to also include a filter like
this one:

filter {
 grok {
   type => nginx_access
   pattern => "%{COMBINEDAPACHELOG}"
 }
}

10. Nginx can be accessed from your host machine via:

http://<your_ip_address>:8088/index.html

Add a new graph to your Kibana dashboard to display a pie chart of the
number of requests for "index.html" vs. "50x.html". You can hit
http://<your_ip_address>:8088/50x.html and http://<your_ip_address>:8088/index.html
to drive up the hit counts. (Hint: check the auto-refresh box in Kibana
and set the refresh time to something low...)
