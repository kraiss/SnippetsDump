#!/usr/bin/python
import argparse
import urllib2
import json
import httplib
import socket


#
# JMX INFO PRIMITIVES
#
def get_memory_used(host, port):
    """ Return the sum of the heap memory used and non heap memory used """
    return get_heap_memory_used(host, port) + get_non_heap_memory_used(host, port)


def get_heap_memory_used(host, port):
    """ Return the heap memory used """
    parameters = ['java.lang:type=Memory', 'HeapMemoryUsage', 'used']
    return _call_jolokia(host, port, parameters)


def get_non_heap_memory_used(host, port):
    """ Return the non heap memory used """
    parameters = ['java.lang:type=Memory', 'NonHeapMemoryUsage', 'used']
    return _call_jolokia(host, port, parameters)


def get_thread_count(host, port):
    """ Return the number of living threads """
    parameters = ['java.lang:type=Threading', 'ThreadCount']
    return _call_jolokia(host, port, parameters)


def get_thread_count_peak(host, port):
    """ Return the peak number of threads """
    parameters = ['java.lang:type=Threading', 'PeakThreadCount']
    return _call_jolokia(host, port, parameters)


def get_loaded_class_count(host, port):
    """ Return the count of loaded classes """
    parameters = ['java.lang:type=ClassLoading', 'LoadedClassCount']
    return _call_jolokia(host, port, parameters)


def get_process_cpu_load(host, port):
    """ Return the process cpu load """
    parameters = ['java.lang:type=OperatingSystem', 'ProcessCpuLoad']
    return _call_jolokia(host, port, parameters)


#
# JOLOKIA CALLER
#
def _call_jolokia(host, port, parameters):
    url = 'http://' + host + ':' + port + '/jolokia/read'
    for p in parameters:
        url += '/' + p

    try:
        response = urllib2.urlopen(url)
        body = response.read().decode()
        return json.loads(body).get('value', 'N/A')
    except urllib2.HTTPError as err:
        return str(err.code) + ' ' + str(err.reason)
    except (urllib2.URLError, httplib.BadStatusLine, socket.error):
        return "Connection error, the server is down or refused connection"


if __name__ == '__main__':
    parser = argparse.ArgumentParser(
        description="""description : Print JMX monitoring using Jolokia""")
    parser.add_argument('-host', dest='host', default='localhost',
                        metavar='host', help="Host of the server : default is localhost")
    parser.add_argument('-p', dest='port', default='7777',
                        metavar='port', help="Port of the server : default is 7777")
    args = parser.parse_args()
    host = args.host
    port = args.port

    print('heap_memory_used: ' + str(get_heap_memory_used(host, port)))
    print('non_heap_memory_used: ' + str(get_non_heap_memory_used(host, port)))
    print('thread_count: ' + str(get_thread_count(host, port)))
    print('thread_count_peak: ' + str(get_thread_count_peak(host, port)))
    print('loaded_class_count: ' + str(get_loaded_class_count(host, port)))
    print('process_cpu_load: ' + str(get_process_cpu_load(host, port)))
