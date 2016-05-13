import json
import urllib2
import argparse


def get_queues_statuses(host='localhost', port='15672', username='guest', password='guest'):
    """
    :param host: Host IP or name
    :param port: Port of contact (default is '15672')
    :param username: Username for authentication (default is 'guest')
    :param password: Password for authentication (default is 'guest')
    :return: RabbitMQ broker queues statuses
    """
    url = 'http://' + host + ':' + port + '/api/queues'
    try:
        # Authenticate
        password_mgr = urllib2.HTTPPasswordMgrWithDefaultRealm()
        password_mgr.add_password(None, url, username, password)
        handler = urllib2.HTTPBasicAuthHandler(password_mgr)
        opener = urllib2.build_opener(handler)
        opener.open(url)
        urllib2.install_opener(opener)

        response = urllib2.urlopen(url)
        return json.loads(response.read().decode())
    except (urllib2.HTTPError, urllib2.URLError) as err:
        raise err


def print_rabbitmq_stats(host, port, username, password):
    try:
        for q in get_queues_statuses(host, port, username, password):
            message = q['name'] + '\t(vhost=\'' + q['vhost'] + '\''
            if q['durable']:
                message += ', durable'
            if q['auto_delete']:
                message += ', auto_delete'
            message += ')'

            message += '\n\tmessages_ready: ' + str(q['messages_ready']) \
                       + '  (rate:' + str(q['messages_ready_details']['rate']) + ')'

            message += '\n\tmessages_unacknowledged: ' + str(q['messages_unacknowledged']) \
                       + '  (rate:' + str(q['messages_unacknowledged_details']['rate']) + ')'
            print message

    except (urllib2.HTTPError, urllib2.URLError) as err:
        print("Connection error, the server is down or refused connection")


if __name__ == '__main__':
    parser = argparse.ArgumentParser(
        description="""description : Print RabbitMQ Queues Status""")
    parser.add_argument('-host', dest='host', default='localhost',
                        metavar='host', help="Host of RabbitMQ (default is localhost")
    parser.add_argument('-port', dest='port', default='15672',
                        metavar='port', help="Port of RabbitMQ Management API (default is 15672")
    parser.add_argument('-username', '-usr', dest='username', default='guest',
                        metavar='username', help="Username for RabbitMQ authentication (default is \'guest\')")
    parser.add_argument('-password', '-pwd', dest='password', default='guest',
                        metavar='password', help="Password for RabbitMQ authentication (default is \'guest\')")
    args = parser.parse_args()
    host = args.host
    port = args.port
    username = args.username
    password = args.password

    print_rabbitmq_stats(host, port, username, password)
