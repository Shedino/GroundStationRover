
/* AUTO-GENERATED FILE.  DO NOT MODIFY.
 *
 * This class was automatically generated by the
 * java mavlink generator tool. It should not be modified by hand.
 */
        
package com.MAVLink;

import java.io.Serializable;
import com.MAVLink.Messages.MAVLinkPayload;
import com.MAVLink.Messages.MAVLinkMessage;
import com.MAVLink.aslaradio.*;

/**
* Common interface for all MAVLink Messages
* Packet Anatomy
* This is the anatomy of one packet. It is inspired by the CAN and SAE AS-4 standards.

* Byte Index  Content              Value       Explanation
* 0            Packet start sign  v1.0: 0xFE   Indicates the start of a new packet.  (v0.9: 0x55)
* 1            Payload length      0 - 255     Indicates length of the following payload.
* 2            Packet sequence     0 - 255     Each component counts up his send sequence. Allows to detect packet loss
* 3            System ID           1 - 255     ID of the SENDING system. Allows to differentiate different MAVs on the same network.
* 4            Component ID        0 - 255     ID of the SENDING component. Allows to differentiate different components of the same system, e.g. the IMU and the autopilot.
* 5            Message ID          0 - 255     ID of the message - the id defines what the payload means and how it should be correctly decoded.
* 6 to (n+6)   Payload             0 - 255     Data of the message, depends on the message id.
* (n+7)to(n+8) Checksum (low byte, high byte)  ITU X.25/SAE AS-4 hash, excluding packet start sign, so bytes 1..(n+6) Note: The checksum also includes MAVLINK_CRC_EXTRA (Number computed from message fields. Protects the packet from decoding a different version of the same packet but with different variables).

* The checksum is the same as used in ITU X.25 and SAE AS-4 standards (CRC-16-CCITT), documented in SAE AS5669A. Please see the MAVLink source code for a documented C-implementation of it. LINK TO CHECKSUM
* The minimum packet length is 8 bytes for acknowledgement packets without payload
* The maximum packet length is 263 bytes for full payload
*
*/
public class MAVLinkPacket implements Serializable {
    private static final long serialVersionUID = 2095947771227815314L;

    public static final int MAVLINK_STX = 254;

    /**
    * Message length. NOT counting STX, LENGTH, SEQ, SYSID, COMPID, MSGID, CRC1 and CRC2
    */
    public int len;

    /**
    * Message sequence
    */
    public int seq;

    /**
    * ID of the SENDING system. Allows to differentiate different MAVs on the
    * same network.
    */
    public int sysid;

    /**
    * ID of the SENDING component. Allows to differentiate different components
    * of the same system, e.g. the IMU and the autopilot.
    */
    public int compid;

    /**
    * ID of the message - the id defines what the payload means and how it
    * should be correctly decoded.
    */
    public int msgid;

    /**
    * Data of the message, depends on the message id.
    */
    public MAVLinkPayload payload;

    /**
    * ITU X.25/SAE AS-4 hash, excluding packet start sign, so bytes 1..(n+6)
    * Note: The checksum also includes MAVLINK_CRC_EXTRA (Number computed from
    * message fields. Protects the packet from decoding a different version of
    * the same packet but with different variables).
    */
    public CRC crc;

    public MAVLinkPacket(){
        payload = new MAVLinkPayload();
    }

    /**
    * Check if the size of the Payload is equal to the "len" byte
    */
    public boolean payloadIsFilled() {
        if (payload.size() >= MAVLinkPayload.MAX_PAYLOAD_SIZE-1) {
            return true;
        }
        return (payload.size() == len);
    }

    /**
    * Update CRC for this packet.
    */
    public void generateCRC(){
        if(crc == null){
            crc = new CRC();
        }
        else{
            crc.start_checksum();
        }
        
        crc.update_checksum(len);
        crc.update_checksum(seq);
        crc.update_checksum(sysid);
        crc.update_checksum(compid);
        crc.update_checksum(msgid);

        payload.resetIndex();
        
        for (int i = 0; i < payload.size(); i++) {
            crc.update_checksum(payload.getByte());
        }
        crc.finish_checksum(msgid);
    }

    /**
    * Encode this packet for transmission.
    *
    * @return Array with bytes to be transmitted
    */
    public byte[] encodePacket() {
        byte[] buffer = new byte[6 + len + 2];
        
        int i = 0;
        buffer[i++] = (byte) MAVLINK_STX;
        buffer[i++] = (byte) len;
        buffer[i++] = (byte) seq;
        buffer[i++] = (byte) sysid;
        buffer[i++] = (byte) compid;
        buffer[i++] = (byte) msgid;
        
        for (int j = 0; j < payload.size(); j++) {
            buffer[i++] = payload.payload.get(j);
        }

        generateCRC();
        buffer[i++] = (byte) (crc.getLSB());
        buffer[i++] = (byte) (crc.getMSB());
        return buffer;
    }

    /**
    * Unpack the data in this packet and return a MAVLink message
    *
    * @return MAVLink message decoded from this packet
    */
    public MAVLinkMessage unpack() {
        switch (msgid) {
                         
            case msg_heartbeat.MAVLINK_MSG_ID_HEARTBEAT:
                return  new msg_heartbeat(this);
                 
            case msg_sys_status.MAVLINK_MSG_ID_SYS_STATUS:
                return  new msg_sys_status(this);
                 
            case msg_system_time.MAVLINK_MSG_ID_SYSTEM_TIME:
                return  new msg_system_time(this);
                 
            case msg_ping.MAVLINK_MSG_ID_PING:
                return  new msg_ping(this);
                 
            case msg_commands.MAVLINK_MSG_ID_COMMANDS:
                return  new msg_commands(this);
                 
            case msg_gps_raw_int.MAVLINK_MSG_ID_GPS_RAW_INT:
                return  new msg_gps_raw_int(this);
                 
            case msg_gps_status.MAVLINK_MSG_ID_GPS_STATUS:
                return  new msg_gps_status(this);
                 
            case msg_attitude.MAVLINK_MSG_ID_ATTITUDE:
                return  new msg_attitude(this);
                 
            case msg_attitude_quaternion.MAVLINK_MSG_ID_ATTITUDE_QUATERNION:
                return  new msg_attitude_quaternion(this);
                 
            case msg_local_position_ned.MAVLINK_MSG_ID_LOCAL_POSITION_NED:
                return  new msg_local_position_ned(this);
                 
            case msg_global_position_int.MAVLINK_MSG_ID_GLOBAL_POSITION_INT:
                return  new msg_global_position_int(this);
                 
            case msg_rc_channels_scaled.MAVLINK_MSG_ID_RC_CHANNELS_SCALED:
                return  new msg_rc_channels_scaled(this);
                 
            case msg_rc_channels_raw.MAVLINK_MSG_ID_RC_CHANNELS_RAW:
                return  new msg_rc_channels_raw(this);
                 
            case msg_set_gps_global_origin.MAVLINK_MSG_ID_SET_GPS_GLOBAL_ORIGIN:
                return  new msg_set_gps_global_origin(this);
                 
            case msg_gps_global_origin.MAVLINK_MSG_ID_GPS_GLOBAL_ORIGIN:
                return  new msg_gps_global_origin(this);
                 
            case msg_attitude_quaternion_cov.MAVLINK_MSG_ID_ATTITUDE_QUATERNION_COV:
                return  new msg_attitude_quaternion_cov(this);
                 
            case msg_nav_controller_output.MAVLINK_MSG_ID_NAV_CONTROLLER_OUTPUT:
                return  new msg_nav_controller_output(this);
                 
            case msg_global_position_int_cov.MAVLINK_MSG_ID_GLOBAL_POSITION_INT_COV:
                return  new msg_global_position_int_cov(this);
                 
            case msg_local_position_ned_cov.MAVLINK_MSG_ID_LOCAL_POSITION_NED_COV:
                return  new msg_local_position_ned_cov(this);
                 
            case msg_rc_channels.MAVLINK_MSG_ID_RC_CHANNELS:
                return  new msg_rc_channels(this);
                 
            case msg_rc_channels_override.MAVLINK_MSG_ID_RC_CHANNELS_OVERRIDE:
                return  new msg_rc_channels_override(this);
                 
            case msg_vfr_hud.MAVLINK_MSG_ID_VFR_HUD:
                return  new msg_vfr_hud(this);
                 
            case msg_radio_status.MAVLINK_MSG_ID_RADIO_STATUS:
                return  new msg_radio_status(this);
                 
            case msg_power_status.MAVLINK_MSG_ID_POWER_STATUS:
                return  new msg_power_status(this);
                 
            case msg_battery_status.MAVLINK_MSG_ID_BATTERY_STATUS:
                return  new msg_battery_status(this);
                 
            case msg_statustext.MAVLINK_MSG_ID_STATUSTEXT:
                return  new msg_statustext(this);
                 
            case msg_radio.MAVLINK_MSG_ID_RADIO:
                return  new msg_radio(this);
                 
            case msg_gps_rtk.MAVLINK_MSG_ID_GPS_RTK:
                return  new msg_gps_rtk(this);
                 
            case msg_gps2_rtk.MAVLINK_MSG_ID_GPS2_RTK:
                return  new msg_gps2_rtk(this);
                 
            case msg_distance_sensor.MAVLINK_MSG_ID_DISTANCE_SENSOR:
                return  new msg_distance_sensor(this);
                 
            case msg_terrain_request.MAVLINK_MSG_ID_TERRAIN_REQUEST:
                return  new msg_terrain_request(this);
                 
            case msg_terrain_data.MAVLINK_MSG_ID_TERRAIN_DATA:
                return  new msg_terrain_data(this);
                 
            case msg_terrain_check.MAVLINK_MSG_ID_TERRAIN_CHECK:
                return  new msg_terrain_check(this);
                 
            case msg_terrain_report.MAVLINK_MSG_ID_TERRAIN_REPORT:
                return  new msg_terrain_report(this);
                 
            case msg_landing_target.MAVLINK_MSG_ID_LANDING_TARGET:
                return  new msg_landing_target(this);
                 
            case msg_debug_vect.MAVLINK_MSG_ID_DEBUG_VECT:
                return  new msg_debug_vect(this);
                 
            case msg_named_value_float.MAVLINK_MSG_ID_NAMED_VALUE_FLOAT:
                return  new msg_named_value_float(this);
                 
            case msg_named_value_int.MAVLINK_MSG_ID_NAMED_VALUE_INT:
                return  new msg_named_value_int(this);
            
            
            default:
                return null;
        }
    }

}
        
        